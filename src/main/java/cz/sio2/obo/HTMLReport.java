package cz.sio2.obo;

import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

    public void writeHTML(Map<String, Version> map, final OutputStream os) throws IOException {
        try {
            final Map<String, Object> dataModel = new HashMap<>();
            final Set<OntologyRecord> records = new HashSet<>();

            final String pattern = "MM-dd-yyyy HH:mm:ss";
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en"));
            final String date = simpleDateFormat.format(new Date());
            dataModel.put("date", date);

            final List<Map.Entry<String, Version>> list = new ArrayList<>(map.entrySet());

            for (Map.Entry<String, Version> entry : list) {
                boolean failed = entry.getValue() == null;
                final OntologyRecord record = new OntologyRecord();
                record
                        .setOntologyIri(entry.getKey());
                if (failed) {
                    record.setType(VersionType.UNKNOWN);
                    record.setVersionIri("");
                } else {
                    record.setType(VersionType.get(entry.getValue().getOwlOntologyIri(), entry.getValue().getOwlVersionIri(), entry.getValue().getOwlVersionInfo() ));
                    String oboVersion = entry.getValue().getOboVersion();
                    record.setVersionIri(oboVersion != null ? oboVersion : "");
                }

                records.add(record);
            }

            dataModel.put("ontologies", records.stream().sorted(comp).collect(Collectors.toList()));

            Template temp = cfg.getTemplate("output-template.html");
            Writer out = new OutputStreamWriter(os);
            temp.process(dataModel, out);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}