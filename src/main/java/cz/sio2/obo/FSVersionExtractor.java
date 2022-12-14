package cz.sio2.obo;

import java.util.regex.Pattern;

public class FSVersionExtractor extends VersionExtractor {

    @Override
    protected Pattern getFormatMatcher() {
        return Pattern.compile(".*Ontology\\(.*");
    }

    @Override
    protected Pattern getIriMatcher() {
        return Pattern.compile(".*Ontology\\(<(.+)> <.+?>.*");
    }

    @Override
    protected Pattern getVersionIriMatcher() {
        return Pattern.compile(".*Ontology\\(<.+> <(.+?)>.*");
    }

    @Override
    protected Pattern getVersionInfoMatcher() {
        return Pattern.compile(".*versionInfo(.+)xxx");
    }
}
