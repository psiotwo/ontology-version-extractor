package cz.sio2.obo.report;

import cz.sio2.obo.OntologyHeader;
import cz.sio2.obo.VersionType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVReport {

    public void write(Map<String, OntologyHeader> map, final OutputStream os) throws IOException {
        try (final Writer writer = new OutputStreamWriter(os)) {
            for (final Map.Entry<String, OntologyHeader> entry : map.entrySet()) {
                final OntologyHeader v = entry.getValue();
                writer.append(v != null ? VersionType.get(v.getOwlOntologyIri()
                                , v.getOwlVersionIri(), v.getOwlVersionInfo()).name() : VersionType.UNKNOWN.name())
                        .append(',')
                        .append(entry.getKey())
                        .append(',')
                        .append((v != null ? v.getOwlVersionIri() : "ERROR"))
                        .append(',')
                        .append((v != null ? v.getOwlVersionInfo() : "ERROR"))
                        .append(',')
                        .append((v != null ? v.getVersion() : "ERROR"))
                        .append(',')
                        .append((v != null && v.getOwlImports() != null ? v.getOwlImports().stream().collect(Collectors.joining("|")) : "ERROR"))
                        .append(',')
                        .append((v != null && v.getNonResolvableImports() != null ? v.getNonResolvableImports().stream().collect(Collectors.joining("|")) : "ERROR"))
                        .append(System.lineSeparator());
            }
        }
    }
}
