package cz.sio2.obo;

import cz.sio2.obo.report.CSVReport;
import cz.sio2.obo.report.HTMLReport;
import cz.sio2.obo.report.TurtleReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class OBOFoundryHeaderExtractor {

    private static void writeRDF(final String file, final Map<String, OntologyHeader> map) throws IOException {
        try (final OutputStream os = new FileOutputStream(file)) {
            new TurtleReport().write(map, os);
        }
    }

    private static void writeCSV(final String file, final Map<String, OntologyHeader> map) throws IOException {
        try (final OutputStream os = new FileOutputStream(file)) {
            new CSVReport().write(map, os);
        }
    }

    private static void writeHTML(final String file, final Map<String, OntologyHeader> map) throws IOException {
        try (final OutputStream os = new FileOutputStream(file)) {
            new HTMLReport().writeHTML(map, os);
        }
    }

    private static List<String> getOntologyUrls(final String registry) {
        final Model model = ModelFactory.createDefaultModel();
        model.read(registry, Lang.TURTLE.toString());
        final String queryString = new Scanner(Objects.requireNonNull(OBOFoundryHeaderExtractor.class.getResourceAsStream("/get-ontology-purls.rq")), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        final QueryExecution qe = QueryExecutionFactory
                .create(queryString, model);
        final ResultSet rs = qe.execSelect();
        final List<String> list = new ArrayList<>();
        while (rs.hasNext()) {
            list.add(rs.next().get("ontology").asLiteral().getString());
        }
        return list;
    }

    private static Map<String, OntologyHeader> fetchHeaders(final List<String> ontologyUrls, final int headerLength) throws MalformedURLException {
        final HeaderFetcher f = new HeaderFetcher();
        final Map<String, OntologyHeader> map = new HashMap<>();
        for (final String url : ontologyUrls) {
            log.info(url);
            OntologyHeader v = f.fetch(new URL(url), headerLength);
            if ( v != null ) {
                if ( v.getOwlOntologyIri() != null ) {
                    map.put(url, v);
                }
            }
        }
        return map;
    }

    public void extract(final String registryUrl, final String outputFile, final int headerLength) throws IOException {
        final List<String> ontologyUrls = getOntologyUrls(registryUrl);
        final Map<String, OntologyHeader> ontologyHeaders = fetchHeaders(ontologyUrls, headerLength);
        writeRDF(outputFile, ontologyHeaders);
    }

    private Map<String, OntologyHeader> loadHeaders(final String inputFile) {
        final Map<String, OntologyHeader> map = new HashMap<>();
        final Model model = ModelFactory.createDefaultModel();
        model.read(inputFile, Lang.TURTLE.toString());
        model.listSubjectsWithProperty(RDF.type, OWL.Ontology).forEach(ontology -> {
            final OntologyHeader header = new OntologyHeader();
            header.setOwlOntologyIri(ontology.getURI());
            final Statement versionIri = ontology.getProperty(OWL2.versionIRI);
            if ( versionIri != null ) {
                header.setOwlVersionIri(versionIri.getObject().asResource().getURI());
            }
            final Statement versionInfo = ontology.getProperty(OWL2.versionInfo);
            if ( versionInfo != null ) {
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

    public void transformToCsv(final String inputFile, final String outputFile) throws IOException {
        writeCSV(outputFile, loadHeaders(inputFile));
    }

    public void transformToHtml(final String inputFile, final String outputFile) throws IOException {
        writeHTML(outputFile, loadHeaders(inputFile));
    }
}
