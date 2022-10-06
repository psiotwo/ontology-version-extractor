package cz.sio2.obo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.sio2.obo.Constants.*;

/**
 * Object that represents the version extracted from the ontology.
 */
@Getter
@Setter
@Slf4j
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

    public String getOboVersionIri() {
        if (owlVersionIri != null) {
            return owlVersionIri;
        }
        if (owlOntologyIri == null || owlVersionInfo == null) {
            return null;
        }

        final Matcher m = Pattern.compile(NS_REGEX).matcher(owlOntologyIri);
        if (!m.matches()) {
            return null;
        }

        final String ontologyId = m.group(2);
        final String baseIri = m.group(1);

        return baseIri + "/GENERATED-" + owlVersionInfo.replaceAll(" ", "-").toLowerCase() + "/" + ontologyId + ".owl";
    }

    public String getVersion() {
        String versionInfoVersion = null;
        if (owlVersionInfo != null) {
            final Matcher m = Pattern.compile(VERSION_FROM_VERSION_INFO_REGEX).matcher(owlVersionInfo);
            if (m.matches()) {
                versionInfoVersion = m.group(1);
            } else {
                versionInfoVersion = owlVersionInfo;
            }
        }

        String versionIriVersion = null;
        if (owlVersionIri != null) {
            final Matcher m = Pattern.compile(VERSION_FROM_VERSION_IRI_REGEX).matcher(owlVersionIri);
            if (m.matches()) {
                versionIriVersion = m.group(2);
            }
        }

        if ( versionIriVersion != null ) {
            if ( versionInfoVersion != null && !versionIriVersion.equals(versionInfoVersion) ) {
                log.info("Versions differ: " + versionIriVersion + " : " + versionInfoVersion + ", using version from versionIri");
            }
            return versionIriVersion;
        } else {
            return versionInfoVersion;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return Objects.equals(owlOntologyIri, version.owlOntologyIri) && Objects.equals(owlVersionIri, version.owlVersionIri) && Objects.equals(owlVersionInfo, version.owlVersionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owlOntologyIri, owlVersionIri, owlVersionInfo);
    }
}
