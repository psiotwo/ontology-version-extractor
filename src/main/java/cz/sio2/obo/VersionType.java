package cz.sio2.obo;

public enum VersionType {

    STANDARD_OBO_VERSION_IRI_1("green", Constants.NS_OBO_HTTP + "(.+)/releases/[0-2][0-9]{3}-[01][0-9]-[0-3][0-9]/\\1.owl"),

    STANDARD_OBO_VERSION_IRI_2("lawngreen", Constants.NS_OBO_HTTP + "(.+)/[0-2][0-9]{3}-[01][0-9]-[0-3][0-9]/\\1.owl"),

    NON_STANDARD_OBO_VERSION_IRI("lightgreen", Constants.NS_OBO_HTTP + "(.+)/.+/\\1.owl"),

    NON_OBO_VERSION_IRI("gold", ".*"),

    NO_VERSION_IRI_BUT_VERSIONINFO("orange", null),

    NO_VERSION_INFORMATION("tomato", null),

    UNKNOWN("red", null);

    private final String versionIriRegex;

    private final String color;

    VersionType(final String color, final String versionIriRegex) {
        this.versionIriRegex = versionIriRegex;
        this.color = color;
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

    public String getVersionIriRegex() {
        return versionIriRegex;
    }

    public String getColor() {
        return color;
    }
}
