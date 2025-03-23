package cz.sio2.ontology.version.model;

import cz.sio2.ontology.version.report.CSVReport;
import cz.sio2.ontology.version.report.HTMLReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Transformer {

  private Map<String, OntologyHeader> loadHeaders(final String inputFile) {
    final Map<String, OntologyHeader> map = new HashMap<>();
    final Model model = ModelFactory.createDefaultModel();
    model.read(inputFile, Lang.TURTLE.toString());
    model.listSubjectsWithProperty(RDF.type, OWL.Ontology).forEach(ontology -> {
      final OntologyHeader header = new OntologyHeader();
      header.setOwlOntologyIri(ontology.getURI());
      final Statement versionIri = ontology.getProperty(OWL2.versionIRI);
      if (versionIri != null) {
        header.setOwlVersionIri(versionIri.getObject().asResource().getURI());
      }
      final Statement versionInfo = ontology.getProperty(OWL2.versionInfo);
      if (versionInfo != null) {
        header.setOwlVersionInfo(versionInfo.getString());
      }
      final StmtIterator imports = ontology.listProperties(OWL2.imports);
      header.setOwlImports(imports.mapWith(s -> s.getResource().getURI()).toList());

      final StmtIterator nonResolvableImports = ontology.listProperties(ResourceFactory.createProperty("https://github.com/psiotwo/ontology-version-extractor/has-nonresolvable-import"));
      header.setNonResolvableImports(nonResolvableImports.mapWith(s -> s.getResource().getURI()).toList());

      map.put(ontology.getURI(), header);
    });
    return map;
  }

  public void transformToCsv(final String inputFile, final String outputFile, Configuration configuration) throws IOException {
    final Map<String, OntologyHeader> map = loadHeaders(inputFile);
    try (final OutputStream os = new FileOutputStream(outputFile)) {
      new CSVReport().write(map, os, configuration);
    }
  }

  public void transformToHtml(final String inputFile, final String outputFile, Configuration configuration) throws IOException {
    final Map<String, OntologyHeader> map = loadHeaders(inputFile);
    try (final OutputStream os = new FileOutputStream(outputFile)) {
      new HTMLReport().writeHTML(map, os, configuration);
    }
  }
}
