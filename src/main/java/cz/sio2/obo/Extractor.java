package cz.sio2.obo;

import cz.sio2.obo.extractor.OntologyHeaderExtractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.sio2.obo.Constants.NS_OBO_HTTP;

public class Extractor {

    private String get(final Pattern pattern, final String singleLine) {
        Matcher m = pattern.matcher(singleLine);
        if (m.matches()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    private String sanitize(final String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&obo;", NS_OBO_HTTP);
    }

    public OntologyHeader extract(final String s, final OntologyHeaderExtractor extractor) {
        final OntologyHeader ontologyHeader = new OntologyHeader();
        final String singleLine = s.replace('\n', ' ');
        if (!extractor.getFormatMatcher().matcher(singleLine).matches()) {
            return null;
        }
        ontologyHeader.setOwlOntologyIri(sanitize(get(extractor.getIriMatcher(), singleLine)));
        ontologyHeader.setOwlVersionIri(sanitize(get(extractor.getVersionIriMatcher(), singleLine)));
        ontologyHeader.setOwlVersionInfo(get(extractor.getVersionInfoMatcher(), singleLine));
        return ontologyHeader;
    }
}
