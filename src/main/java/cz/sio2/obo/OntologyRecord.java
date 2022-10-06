package cz.sio2.obo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OntologyRecord {

    public VersionType type;

    public String ontologyIri;

    public String versionIri;

    public String version;
}
