package cz.sio2.obo;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static cz.sio2.obo.Constants.HEADER_LENGTH;

@Slf4j
public class OBOFoundryVersionFetcher {

    //    private static final String registry = "https://raw.githubusercontent.com/OBOFoundry/OBOFoundry.github.io/master/registry/ontologies.ttl";
    private static final String registry = "https://obofoundry.org/registry/ontologies.ttl";

    private static void writeCSV(final String file, Map<String,String> map) {
        try (Writer writer = new FileWriter(file)) {
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

    private static void writeHTML(final String file, Map<String,String> map) {
        try (Writer writer = new FileWriter(file)) {
            writer.write("<html><head></head><body><h1>Latest OBO ontology versions</h1>" +
                    "<span style=\"color:green\">green = Version IRI successfully extracted from owl:versionIri.</span><br/>" +
                    "<span style=\"color:orange\">green = Version IRI successfully generated from owl:versionInfo.</span><br/>" +
                    "<span style=\"color:red\">green = Version IRI cannot be extracted.</span><br/><br/>" +
                    "<hr/><table>");
            List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
            list.sort(Map.Entry.comparingByKey());

            for (Map.Entry<String, String> entry : list) {
                String c;
                boolean failed = entry.getValue() == null || entry.getValue().equals("null");
                if ( failed ) {
                    c = "red";
                } else if ( entry.getValue().contains("GENERATED") ) {
                    c = "orange";
                } else {
                    c = "green";
                }
                writer.append("<tr style=\"color:" + c + ";\"><td>");
                writer.append(entry.getKey());
                writer.append("</td><td>");
                if ( !failed ) {
                    writer.append(entry.getValue());
                }
                writer.append("</td></tr>");
            }
            String pattern = "MM-dd-yyyy HH:mm:ss";
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern, new Locale("en"));
            String date = simpleDateFormat.format(new Date());
            writer.append("</table><hr/><a href=\"https://github.com/psiotwo/ontology-version-extractor\">GitHub project</a><br/>");
            writer.append("Updated: " + date + "</body></html>");
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

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
        writeCSV(args[0], map);
        writeHTML(args[0]+".html", map);
    }
}
