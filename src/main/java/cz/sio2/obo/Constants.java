package cz.sio2.obo;

public class Constants {

    public static final int HEADER_LENGTH = 4096;

    public static final String NS_OBO = "https://purl.obolibrary.org/obo/";

    public static final String NS_OBO_HTTP = "http://purl.obolibrary.org/obo/";

    public static final String NS_OBO_REGEX = "(" + NS_OBO + "|" + NS_OBO_HTTP + "(.*))[.]owl";

    public static final String NS_REGEX = "(.*/([^/]+))[.]owl";
}
