package cz.sio2.obo;

public class Constants {

    public static final int HEADER_LENGTH = 4096;

    public static final String NS_OBO_HTTP = "http://purl.obolibrary.org/obo/";

    /**
     * Regex for extracting ontology name from an IRI
     */
    public static final String NS_REGEX = "(.*/([^/.]+))([.]owl)?/?";

    /**
     * Regex for extracting ontology version from an IRI
     */
    public static final String VERSION_FROM_VERSION_IRI_REGEX = "https?://purl[.]obolibrary[.]org/obo/(.+/)?([^/]+)/([^/]+)[.]owl";

    /**
     * Regex for extracting ontology version from an IRI
     */
    public static final String VERSION_FROM_VERSION_INFO_REGEX = "[^0-9]*[v]?([0-9]+[.]?[0-9]*[.]?[0-9]*[.]?[0-9]*)[^0-9]*";
}
