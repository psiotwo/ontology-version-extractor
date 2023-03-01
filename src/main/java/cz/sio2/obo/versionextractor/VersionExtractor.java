package cz.sio2.obo.versionextractor;

import cz.sio2.obo.Version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.sio2.obo.Constants.NS_OBO_HTTP;

public abstract class VersionExtractor {

    protected abstract Pattern getFormatMatcher();

    protected abstract Pattern getIriMatcher();

    protected abstract Pattern getVersionIriMatcher();

    protected abstract Pattern getVersionInfoMatcher();

    protected String get(final Pattern pattern, final String singleLine) {
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

    public boolean extract(final String s, final Version version) {
        final String singleLine = s.replace('\n', ' ');
        if (!getFormatMatcher().matcher(singleLine).matches()) {
            return false;
        }
        version.setOwlOntologyIri(sanitize(get(getIriMatcher(), singleLine)));
        version.setOwlVersionIri(sanitize(get(getVersionIriMatcher(), singleLine)));
        version.setOwlVersionInfo(get(getVersionInfoMatcher(), singleLine));
        return true;
    }
}
