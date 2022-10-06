package cz.sio2.obo;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static cz.sio2.obo.Constants.HEADER_LENGTH;

@Slf4j
public class OBOFoundryVersionFetcher {

    //    private static final String registry = "https://raw.githubusercontent.com/OBOFoundry/OBOFoundry.github.io/master/registry/ontologies.ttl";
    private static final String registry = "https://obofoundry.org/registry/ontologies.ttl";

    private static void writeCSV(final String file, Map<String, Version> map) {
        try (Writer writer = new FileWriter(file)) {
            for (Map.Entry<String, Version> entry : map.entrySet()) {
                final Version v = entry.getValue();
                writer.append(v != null ? VersionType.get(v.getOwlOntologyIri()
                                , v.getOwlVersionIri(), v.getOwlVersionInfo()).name() : VersionType.UNKNOWN.name())
                        .append(',')
                        .append(entry.getKey())
                        .append(',')
                        .append((v != null ? v.getVersion() : "ERROR"))
                        .append(',')
                        .append((v != null ? v.getOboVersionIri() : "ERROR"))
                        .append(System.lineSeparator());
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private static void writeHTML(final String file, Map<String, Version> map) {
        try {
            new HTMLReport().writeHTML(map, new FileOutputStream(file));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        final Model model = ModelFactory.createOntologyModel();
        model.read(registry, "TURTLE");
        final String queryString = new Scanner(Objects.requireNonNull(OBOFoundryVersionFetcher.class.getResourceAsStream("/get-ontology-purls.rq")), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        final QueryExecution qe = QueryExecutionFactory
                .create(queryString, model);
        final ResultSet rs = qe.execSelect();

        final VersionFetcher f = new VersionFetcher();

        final Map<String, Version> map = new HashMap<>();

        while (rs.hasNext()) {
            final String url = rs.next().get("ontology").asLiteral().getString();
            log.info(url);
            final Version v = f.fetch(new URL(url), HEADER_LENGTH);
            map.put(url, v);
        }

        log.info("FINISHED.");
        writeCSV(args[0], map);
        writeHTML(args[0] + ".html", map);
    }
}
