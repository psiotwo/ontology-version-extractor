package cz.sio2.obo.extractor;

import java.util.regex.Pattern;

public class TurtleHeaderExtractor implements OntologyHeaderExtractor {

    @Override
    public Pattern getFormatMatcher() {
        return Pattern.compile(".*((@prefix|@base).*)|.\\s*$");
    }

    @Override
    public Pattern getIriMatcher() {
        return Pattern.compile("^.*<([^>]+)>\\s+(?:a|rdf:type)\\s+owl:Ontology.*$");
    }

    @Override
    public Pattern getVersionIriMatcher() {
        return Pattern.compile(".*[a-zA-Z0-9]*:?versionIRI\\s+<([^>]+)>.*");
    }

    @Override
    public Pattern getVersionInfoMatcher() {
        return Pattern.compile(".*[a-zA-Z0-9]*:?versionInfo\\s+\"([^\"]+?)\".*");
    }

    @Override
    public Pattern getImportsMatcher() {
        return Pattern.compile(".*[a-zA-Z0-9]*:?imports\\s+\"([^\"]+?)\".*");
    }
}
