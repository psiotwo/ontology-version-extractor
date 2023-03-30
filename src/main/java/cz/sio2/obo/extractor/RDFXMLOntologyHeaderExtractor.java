package cz.sio2.obo.extractor;

import java.util.regex.Pattern;

public class RDFXMLOntologyHeaderExtractor implements OntologyHeaderExtractor {

    @Override
    public Pattern getFormatMatcher() {
        return Pattern.compile(".*<rdf:RDF.*");
    }

    @Override
    public Pattern getIriMatcher() {
        return Pattern.compile(".*<[a-zA-Z0-9]*:?Ontology [a-zA-Z0-9]*:?about=\"([^\"]+?)\"(/)?>.*");
    }

    @Override
    public Pattern getVersionIriMatcher() {
        return Pattern.compile(".*<[a-zA-Z0-9]*:?versionIRI [a-zA-Z0-9]*:?resource=\"([^\"]+?)\"/>.*");
    }

    @Override
    public Pattern getVersionInfoMatcher() {
        return Pattern.compile(".*<[a-zA-Z0-9]*:?versionInfo[^>]*>(.+?)</[a-zA-Z0-9]*:?versionInfo>.*");
    }

    @Override
    public Pattern getImportsMatcher() {
        return Pattern.compile("<[a-zA-Z0-9]*:?imports [a-zA-Z0-9]*:?resource=\"([^\"]+?)\"/>");
    }
}
