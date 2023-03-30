package cz.sio2.obo.extractor;

import java.util.regex.Pattern;

public class FSOntologyHeaderExtractor implements OntologyHeaderExtractor {

    @Override
    public Pattern getFormatMatcher() {
        return Pattern.compile(".*Ontology\\(.*");
    }

    @Override
    public Pattern getIriMatcher() {
        return Pattern.compile(".*Ontology\\(<(.+)> <.+?>.*");
    }

    @Override
    public Pattern getVersionIriMatcher() {
        return Pattern.compile(".*Ontology\\(<.+> <(.+?)>.*");
    }

    @Override
    public Pattern getVersionInfoMatcher() {
        return Pattern.compile(".*versionInfo(.+)xxx");
    }

    @Override
    public Pattern getImportsMatcher() {
        // TODO - implement
        return Pattern.compile("NOT_IMPLEMENTED");
    }
}
