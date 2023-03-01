package cz.sio2.obo.versionextractor;

import java.util.regex.Pattern;

public class XMLVersionExtractor extends VersionExtractor {

    @Override
    protected Pattern getFormatMatcher() {
        return Pattern.compile(".*<Ontology.*");
    }

    @Override
    protected Pattern getIriMatcher() {
        return Pattern.compile(".*<Ontology .* ontologyIRI=\"([^\"]+?)\".*");
    }

    @Override
    protected Pattern getVersionIriMatcher() {
        return Pattern.compile(".*<Ontology [^>]* versionIRI=\"([^\"]+?)\".*");
    }

    @Override
    protected Pattern getVersionInfoMatcher() {
        return Pattern.compile(".*<Annotation>\\s+<AnnotationProperty\\s+abbreviatedIRI=\"owl:versionInfo\"/>\\s+<Literal[^>]*>([^<]+?)</Literal>.*");
    }
}