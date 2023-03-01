package cz.sio2.obo.extractor;

import java.util.regex.Pattern;

public class FSExtractor extends Extractor {

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
