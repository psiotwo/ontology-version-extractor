package cz.sio2.obo.extractor;

import java.util.regex.Pattern;

public class RDFXMLExtractor extends Extractor {

    @Override
    protected Pattern getFormatMatcher() {
        return Pattern.compile(".*<rdf:RDF.*");
    }

    @Override
    protected Pattern getIriMatcher() {
        return Pattern.compile(".*<[a-zA-Z0-9]*:?Ontology [a-zA-Z0-9]*:?about=\"([^\"]+?)\">.*");
    }

    @Override
    protected Pattern getVersionIriMatcher() {
        return Pattern.compile(".*<[a-zA-Z0-9]*:?versionIRI [a-zA-Z0-9]*:?resource=\"([^\"]+?)\"/>.*");
    }

    @Override
    protected Pattern getVersionInfoMatcher() {
        return Pattern.compile(".*<[a-zA-Z0-9]*:?versionInfo[^>]*>(.+?)</[a-zA-Z0-9]*:?versionInfo>.*");
    }
}
