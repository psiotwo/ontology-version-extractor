package cz.sio2.obo.extractor;

import java.util.regex.Pattern;

public class XMLOntologyHeaderExtractor implements OntologyHeaderExtractor {

    @Override
    public Pattern getFormatMatcher() {
        return Pattern.compile(".*<Ontology.*");
    }

    @Override
    public Pattern getIriMatcher() {
        return Pattern.compile(".*<Ontology .* ontologyIRI=\"([^\"]+?)\".*");
    }

    @Override
    public Pattern getVersionIriMatcher() {
        return Pattern.compile(".*<Ontology [^>]* versionIRI=\"([^\"]+?)\".*");
    }

    @Override
    public Pattern getVersionInfoMatcher() {
        return Pattern.compile(".*<Annotation>\\s+<AnnotationProperty\\s+abbreviatedIRI=\"owl:versionInfo\"/>\\s+<Literal[^>]*>([^<]+?)</Literal>.*");
    }
}