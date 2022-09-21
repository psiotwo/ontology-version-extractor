package cz.sio2.obo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean extract(final String s, final Version version) {
        final String singleLine = s.replace('\n', ' ');
        if (!getFormatMatcher().matcher(singleLine).matches()) {
            return false;
        }
        version.setOwlOntologyIri(get(getIriMatcher(),singleLine));
        version.setOwlVersionIri(get(getVersionIriMatcher(),singleLine));
        version.setOwlVersionInfo(get(getVersionInfoMatcher(),singleLine));
        return true;
    }
}
