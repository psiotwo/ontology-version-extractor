package cz.sio2.ontology.version.report;

import cz.sio2.ontology.version.model.OntologyHeader;
import cz.sio2.ontology.version.obo.VersionType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

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
                        .append((v != null && v.getOwlImports() != null ? String.join("|", v.getOwlImports()) : "ERROR"))
                        .append(',')
                        .append((v != null && v.getNonResolvableImports() != null ? String.join("|", v.getNonResolvableImports()) : "ERROR"))
                        .append(System.lineSeparator());
            }
        }
    }
}
