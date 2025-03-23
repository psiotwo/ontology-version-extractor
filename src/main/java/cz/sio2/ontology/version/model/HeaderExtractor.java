package cz.sio2.ontology.version.model;

import cz.sio2.ontology.version.report.TurtleReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Slf4j
public class HeaderExtractor {

  private static List<String> getOntologyUrls(final Configuration configuration) {
    final Model model = ModelFactory.createDefaultModel();
    model.read(configuration.getOntologyCatalogueIri().toString(), Lang.TURTLE.toString());
    final String queryString = configuration.getOntologyIriQuery();
    try (QueryExecution qe = QueryExecutionFactory.create(queryString, model)) {
      final ResultSet rs = qe.execSelect();
      final List<String> list = new ArrayList<>();
      while (rs.hasNext()) {
        list.add(rs.next().get("ontologyIri").asLiteral().getString());
      }
      return list;
    }
  }

  private static Map<String, OntologyHeader> fetchHeaders(final Configuration configuration, final List<String> ontologyUrls) throws MalformedURLException {
    final HeaderFetcher f = new HeaderFetcher();
    final Map<String, OntologyHeader> map = new HashMap<>();
    for (final String url : ontologyUrls) {
      log.info(url);
      OntologyHeader v = f.fetch(new URL(url), configuration.getHeaderSize());
      if (v != null) {
        if (v.getOwlOntologyIri() != null) {
          map.put(url, v);
        }
      }
    }
    return map;
  }

  public void extract(final Configuration configuration, final String outputFile) throws IOException {
    final List<String> ontologyUrls = getOntologyUrls(configuration);
    final Map<String, OntologyHeader> ontologyHeaders = fetchHeaders(configuration, ontologyUrls);
    try (final OutputStream os = new FileOutputStream(outputFile)) {
      new TurtleReport().write(ontologyHeaders, os);
    }
  }
}
