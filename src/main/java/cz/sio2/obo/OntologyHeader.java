package cz.sio2.obo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.sio2.obo.Constants.*;

/**
 * Object that represents the ontology header extracted from the ontology.
 */
@Getter
@Setter
@Slf4j
public class OntologyHeader {

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

    /**
     * owl:imports
     */
    private List<String> owlImports = new ArrayList<>();

    /**
     * nonresolvable owl:imports
     */
    private List<String> nonResolvableImports = new ArrayList<>();

    /**
     * Generates an OBO-compliant version IRI. Returns
     * - owl:versionIri if available
     * - a new version IRI based on the IRI of owl:Ontology resource and owl:versionInfo
     * - null if neither owl:versionInfo nor owl:versionIri is available
     *
     * @return OBO-compliant version IRI
     */
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

    /**
     * Returns version of the ontology according to the following algorithm:
     * - if version can be extracted from owl:versionIri, it is returned.
     * - if version can be extracted from owl:versionInfo, it is returned.
     * - if version extracted from owl:versionInfo and owl:versionIri differs, a warning is logged.
     *
     * @return version of the ontology.
     */
    public String getVersion() {
        final String versionInfoVersion = extractVersionFromVersionInfo();
        final String versionIriVersion = extractVersionFromVersionIri();
        if (versionIriVersion != null) {
            if (versionInfoVersion != null && !versionIriVersion.equals(versionInfoVersion)) {
                log.info("Versions differ: {} : {}, using version from versionIri", versionIriVersion, versionInfoVersion);
            }
            return versionIriVersion;
        } else {
            return versionInfoVersion;
        }
    }

    String extractVersionFromVersionInfo() {
        String versionInfoVersion = null;
        if (owlVersionInfo != null) {
            final Matcher m = Pattern.compile(VERSION_FROM_VERSION_INFO_REGEX).matcher(owlVersionInfo);
            if (m.matches()) {
                versionInfoVersion = m.group(1);
            } else {
                versionInfoVersion = owlVersionInfo;
            }
        }
        return versionInfoVersion;
    }

    String extractVersionFromVersionIri() {
        String versionIriVersion = null;
        if (owlVersionIri != null) {
            final Matcher m = Pattern.compile(VERSION_FROM_VERSION_IRI_REGEX).matcher(owlVersionIri);
            if (m.matches()) {
                versionIriVersion = m.group(2);
            }
        }
        return versionIriVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OntologyHeader version = (OntologyHeader) o;
        return Objects.equals(owlOntologyIri, version.owlOntologyIri)
                && Objects.equals(owlVersionIri, version.owlVersionIri)
                && Objects.equals(owlVersionInfo, version.owlVersionInfo)
                && Objects.equals(owlImports, version.owlImports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owlOntologyIri, owlVersionIri, owlVersionInfo, owlImports);
    }
}
