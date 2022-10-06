package cz.sio2.obo;

public enum VersionType {

    STANDARD_OBO_VERSION_IRI_1(
            "OBO (Type 1)",
            "owl:versionIri compliant with the OBO Foundry Identifier Policy.",
            "green",
            Constants.NS_OBO_HTTP + "(.+)/releases/[0-2][0-9]{3}-[01][0-9]-[0-3][0-9]/\\1.owl"),

    STANDARD_OBO_VERSION_IRI_2(
            "OBO (Type 2)",
            "owl:versionIri compliant with the OBO Foundry Identifier Policy.",
            "light-green",
            Constants.NS_OBO_HTTP + "(.+)/[0-2][0-9]{3}-[01][0-9]-[0-3][0-9]/\\1.owl"),

    NON_STANDARD_OBO_VERSION_IRI(
            "OBO (Non-standard)",
            "owl:versionIri present, with partial compliance with the OBO Foundry Identifier Policy",
            "lime",
            Constants.NS_OBO_HTTP + "(.+)/.+/\\1.owl"),

    NON_OBO_VERSION_IRI(
            "Non-OBO",
            "owl:versionIri present, but not OBO compliant.",
            "amber",
            ".*"),

    NO_VERSION_IRI_BUT_VERSIONINFO(
            "Version Info Only",
            "No/invalid owl:versionIri present, only owl:versionInfo.",
            "orange",
            null),

    NO_VERSION_INFORMATION(
            "No Version",
            "Neither owl:versionIri nor owl:versionInfo present.",
            "deep-orange",
            null),

    UNKNOWN(
            "Unknown",
            "A problem (e.g. failing connection) occurred when fetching ontology header.",
            "red",
            null);

    private final String shortName;

    private final String description;

    private final String versionIriRegex;

    private final String color;

    VersionType(final String shortName, final String description, final String color, final String versionIriRegex) {
        this.shortName = shortName;
        this.description = description;
        this.color = color;
        this.versionIriRegex = versionIriRegex;
    }

    public static VersionType get(final String ontologyIri, final String versionIri, final String versionInfo) {
        if (versionIri != null) {
            if (versionIri.matches(STANDARD_OBO_VERSION_IRI_1.getVersionIriRegex())) {
                return STANDARD_OBO_VERSION_IRI_1;
            } else if (versionIri.matches(STANDARD_OBO_VERSION_IRI_2.getVersionIriRegex())) {
                return STANDARD_OBO_VERSION_IRI_2;
            } else if (versionIri.matches(NON_STANDARD_OBO_VERSION_IRI.getVersionIriRegex())) {
                return NON_STANDARD_OBO_VERSION_IRI;
            } else if (versionIri.matches(NON_OBO_VERSION_IRI.getVersionIriRegex())) {
                return NON_OBO_VERSION_IRI;
            }
        }

        if (ontologyIri != null && versionInfo != null && !versionInfo.isEmpty()) {
            return NO_VERSION_IRI_BUT_VERSIONINFO;
        } else if (ontologyIri != null) {
            return NO_VERSION_INFORMATION;
        } else {
            return UNKNOWN;
        }
    }

    public String getShortName() {
        return shortName;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getVersionIriRegex() {
        return versionIriRegex;
    }
}
