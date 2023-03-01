package cz.sio2.obo;

import cz.sio2.obo.report.CSVReport;
import cz.sio2.obo.report.HTMLReport;
import cz.sio2.obo.report.TurtleReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
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
public class OBOFoundryVersionExtractor {

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
        final String queryString = new Scanner(Objects.requireNonNull(OBOFoundryVersionExtractor.class.getResourceAsStream("/get-ontology-purls.rq")), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        final QueryExecution qe = QueryExecutionFactory
                .create(queryString, model);
        final ResultSet rs = qe.execSelect();
        final List<String> list = new ArrayList<>();
        while (rs.hasNext()) {
            list.add(rs.next().get("ontology").asLiteral().getString());
        }
        return list;
    }

    private static Map<String, OntologyHeader> fetchVersions(final List<String> ontologyUrls, final int headerLength) throws MalformedURLException {
        final VersionFetcher f = new VersionFetcher();
        final Map<String, OntologyHeader> map = new HashMap<>();
        for (final String url : ontologyUrls) {
            log.info(url);
            final OntologyHeader v = f.fetch(new URL(url), headerLength);
            map.put(url, v);
        }
        return map;
    }

    public void extract(final String registryUrl, final String outputFile, final int headerLength) throws IOException {
        final List<String> ontologyUrls = getOntologyUrls(registryUrl);
        final Map<String, OntologyHeader> ontologyVersions = fetchVersions(ontologyUrls, headerLength);
        writeRDF(outputFile, ontologyVersions);
    }

    private Map<String, OntologyHeader> loadVersions(final String inputFile) {
        final Map<String, OntologyHeader> map = new HashMap<>();
        final Model model = ModelFactory.createDefaultModel();
        model.read(inputFile, Lang.TURTLE.toString());
        model.listSubjectsWithProperty(RDF.type, OWL.Ontology).forEach(ontology -> {
            final OntologyHeader version = new OntologyHeader();
            version.setOwlOntologyIri(ontology.getURI());
            final Statement versionIri = ontology.getProperty(OWL2.versionIRI);
            if ( versionIri != null ) {
                version.setOwlVersionIri(versionIri.getObject().asResource().getURI());
            }
            final Statement versionInfo = ontology.getProperty(OWL2.versionInfo);
            if ( versionInfo != null ) {
                version.setOwlVersionInfo(versionInfo.getString());
            }
            map.put(ontology.getURI(), version);
        });
        return map;
    }

    public void transformToCsv(final String inputFile, final String outputFile) throws IOException {
        writeCSV(outputFile, loadVersions(inputFile));
    }

    public void transformToHtml(final String inputFile, final String outputFile) throws IOException {
        writeHTML(outputFile, loadVersions(inputFile));
    }
}
