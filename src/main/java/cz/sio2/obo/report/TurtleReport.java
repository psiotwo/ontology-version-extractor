package cz.sio2.obo.report;

import cz.sio2.obo.Version;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;

import java.io.OutputStream;
import java.util.Map;

public class TurtleReport {

    public void write(Map<String, Version> map, final OutputStream os) {
        final Model model = createModel(map);
        RDFDataMgr.write(os, model, Lang.TURTLE);
    }

    private Model createModel(Map<String, Version> map) {
        final Model model = ModelFactory.createDefaultModel();
        for (final Map.Entry<String, Version> entry : map.entrySet()) {
            final Resource ontology = ResourceFactory.createResource(entry.getKey());
            model.add(ontology, RDF.type, OWL.Ontology);

            final Version version = entry.getValue();
            if ( version != null ) {
                final String versionIriString = version.getOwlVersionIri();
                if (versionIriString != null && !versionIriString.isEmpty()) {
                    model.add(ontology, OWL2.versionIRI, ResourceFactory.createResource(versionIriString));
                }

                final String versionInfo = version.getOwlVersionInfo();
                if (versionInfo != null && !versionInfo.isEmpty()) {
                    model.add(ontology, OWL2.versionInfo, versionInfo);
                }
            }
        }
        return model;
    }
}
