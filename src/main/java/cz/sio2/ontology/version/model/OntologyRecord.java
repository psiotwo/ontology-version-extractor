package cz.sio2.ontology.version.model;

import cz.sio2.ontology.version.obo.VersionType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

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
