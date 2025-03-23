package cz.sio2.ontology.version.report;

import cz.sio2.ontology.version.model.OntologyRecord;
import cz.sio2.ontology.version.model.OntologyHeader;
import cz.sio2.ontology.version.model.VersionType;
import freemarker.template.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class HTMLReport {

  static Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

  static {
    cfg.setClassForTemplateLoading(HTMLReport.class, "/");
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  }

  public void writeHTML(Map<String, OntologyHeader> map, final OutputStream os, cz.sio2.ontology.version.model.Configuration configuration) throws IOException {
    try {
      final Map<String, Object> dataModel = new HashMap<>();
      final Set<OntologyRecord> records = new HashSet<>();

      final String pattern = "MM-dd-yyyy HH:mm:ss";
      final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en"));
      final String date = simpleDateFormat.format(new Date());
      dataModel.put("date", date);

      final List<Map.Entry<String, OntologyHeader>> list = new ArrayList<>(map.entrySet());

      for (Map.Entry<String, OntologyHeader> entry : list) {
        boolean failed = entry.getValue() == null;
        final OntologyRecord record = new OntologyRecord();
        record
                .setOntologyIri(entry.getKey());
        if (failed) {
          record
                  .setType(VersionType.UNKNOWN)
                  .setVersionIri("")
                  .setVersionInfo("")
                  .setVersion("")
                  .setImports(Collections.emptyList())
                  .setNonResolvableImports(Collections.emptyList());
        } else {
          record
                  .setType(VersionType.get(entry.getValue().getOwlOntologyIri(), entry.getValue().getOwlVersionIri(), entry.getValue().getOwlVersionInfo(), configuration))
                  .setVersionIri(coalesce(entry.getValue().getOwlVersionIri()))
                  .setVersion(coalesce(entry.getValue().getVersion(configuration)))
                  .setVersionInfo(coalesce(entry.getValue().getOwlVersionInfo()))
                  .setImports(entry.getValue().getOwlImports())
                  .setNonResolvableImports(entry.getValue().getNonResolvableImports());
        }

        records.add(record);
      }

      Comparator<OntologyRecord> comp = Comparator
              .comparing((OntologyRecord r) -> VersionType.getAll(configuration).indexOf(r.getType()))
              .thenComparing(OntologyRecord::getOntologyIri, Comparator.nullsLast(String::compareTo))
              .thenComparing(OntologyRecord::getVersionIri, Comparator.nullsLast(String::compareTo));

      dataModel.put("ontologies", records.stream().sorted(comp).collect(Collectors.toList()));
      dataModel.put("types", VersionType.getAll(configuration));

      Template temp = cfg.getTemplate("output-template.html");
      Writer out = new OutputStreamWriter(os);
      temp.process(dataModel, out);
    } catch (TemplateException e) {
      throw new RuntimeException(e);
    }
  }

  private String coalesce(String s) {
    return s != null ? s : "";
  }
}
