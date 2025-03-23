package cz.sio2.ontology.version;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
    public static String sanitize(final String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&obo;", "http://purl.obolibrary.org/obo/");
    }
}
