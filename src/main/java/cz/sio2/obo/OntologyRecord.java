package cz.sio2.obo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class OntologyRecord {

    public VersionType type;

    public String ontologyIri;

    public String versionIri;

    public String versionInfo;

    public String version;

    public List<String> imports;

    public List<String> nonResolvableImports;
}
