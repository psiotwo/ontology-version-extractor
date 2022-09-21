package cz.sio2.obo;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static cz.sio2.obo.Constants.HEADER_LENGTH;

@Slf4j
public class OBOFoundryVersionFetcher {

    //    private static final String registry = "https://raw.githubusercontent.com/OBOFoundry/OBOFoundry.github.io/master/registry/ontologies.ttl";
    private static final String registry = "https://obofoundry.org/registry/ontologies.ttl";

    public static void main(String[] args) throws MalformedURLException {
        final Model model = ModelFactory.createOntologyModel();
        model.read(registry, "TURTLE");
        final String queryString = new Scanner(OBOFoundryVersionFetcher.class.getResourceAsStream("/get-ontology-purls.rq"), "UTF-8").useDelimiter("\\A").next();
        final QueryExecution qe = QueryExecutionFactory
                .create(queryString, model);
        final ResultSet rs = qe.execSelect();

        final VersionFetcher f = new VersionFetcher();

        final Map<String, String> map = new HashMap<>();

        while (rs.hasNext()) {
            final String url = rs.next().get("ontology").asLiteral().getString();
            log.info(url);
            final Version v = f.fetch(new URL(url), HEADER_LENGTH);
            map.put(url, (v != null ? v.getOboVersion() : "ERROR"));
        }

        log.info("FINISHED.");
        try (Writer writer = new FileWriter(args[0])) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writer.append(entry.getKey())
                        .append(',')
                        .append(entry.getValue())
                        .append(System.lineSeparator());
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
