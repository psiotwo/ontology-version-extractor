package cz.sio2.obo.report;

import cz.sio2.obo.OntologyRecord;
import cz.sio2.obo.OntologyHeader;
import cz.sio2.obo.VersionType;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class HTMLReport {

    static Configuration cfg = new Configuration();

    static Comparator<OntologyRecord> comp =
            Comparator.comparing(OntologyRecord::getType, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(OntologyRecord::getOntologyIri)
                    .thenComparing(OntologyRecord::getVersionIri);

    static {
        cfg.setClassForTemplateLoading(HTMLReport.class, "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public void writeHTML(Map<String, OntologyHeader> map, final OutputStream os) throws IOException {
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
                    record.setType(VersionType.UNKNOWN);
                    record.setVersionIri("");
                    record.setVersionInfo("");
                    record.setVersion("");
                } else {
                    record.setType(VersionType.get(entry.getValue().getOwlOntologyIri(), entry.getValue().getOwlVersionIri(), entry.getValue().getOwlVersionInfo()));
                    String versionIri = entry.getValue().getOwlVersionIri();
                    record.setVersionIri(versionIri != null ? versionIri : "");
                    String version = entry.getValue().getVersion();
                    record.setVersion(version != null ? version : "");
                    String versionInfo = entry.getValue().getOwlVersionInfo();
                    record.setVersionInfo(versionInfo != null ? versionInfo : "");
                }

                records.add(record);
            }

            dataModel.put("ontologies", records.stream().sorted(comp).collect(Collectors.toList()));
            dataModel.put("types", Arrays.asList(VersionType.values()));

            Template temp = cfg.getTemplate("output-template.html");
            Writer out = new OutputStreamWriter(os);
            temp.process(dataModel, out);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
