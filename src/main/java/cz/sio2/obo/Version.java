package cz.sio2.obo;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.sio2.obo.Constants.NS_OBO_REGEX;
import static cz.sio2.obo.Constants.NS_REGEX;

/**
 * Object that represents the version extracted from the ontology.
 */
@Getter
@Setter
public class Version {

    /**
     * owl:Ontology IRI.
     */
    private String owlOntologyIri;

    /**
     * owl:versionIri value.
     */
    private String owlVersionIri;

    /**
     * owl:versionInfo value
     */
    private String owlVersionInfo;

    public String getOboVersion() {
        if ( owlVersionIri != null ) {
            return owlVersionIri;
        }
        if ( owlOntologyIri == null || owlVersionInfo == null) {
            return null;
        }

        final Matcher m = Pattern.compile(NS_REGEX).matcher(owlOntologyIri);
        if (!m.matches()) {
            return null;
        }

        final String ontologyId = m.group(2);
        final String baseIri = m.group(1);

        return baseIri + "/GENERATED-" + owlVersionInfo.replaceAll(" ","-").toLowerCase() + "/" + ontologyId + ".owl";
    }
}
