package cz.sio2.ontology.version.model;

import cz.sio2.ontology.version.obo.HeaderFetcher;
import cz.sio2.ontology.version.obo.Utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Extracts ontology header from the given ontology.
 */
public class Extractor {

    private String get(final Pattern pattern, final String singleLine) {
        Matcher m = pattern.matcher(singleLine);
        if (m.matches()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    private List<String> getMultiple(final Pattern pattern, final String singleLine) {
        final Matcher m = pattern.matcher(singleLine);
        final List<String> matches = new ArrayList<>();
        while(m.find()) {
            matches.add(m.group(1));
        }
        return matches;
    }

    /**
     * Extracts ontology header from the given ontology content.
     *
     * @param ontologyContentSnippet ontology content to recognize versioning information in.
     * @param extractor the actual extractor to use.
     * @return ontology version header as extracted by the given extractor.
     */
    public OntologyHeader extract(final String ontologyContentSnippet, final OntologyHeaderExtractor extractor) {
        final OntologyHeader ontologyHeader = new OntologyHeader();
        final String singleLine = ontologyContentSnippet.replace('\n', ' ');
        if (!extractor.getFormatMatcher().matcher(singleLine).matches()) {
            return null;
        }
        ontologyHeader.setOwlOntologyIri(Utils.sanitize(get(extractor.getIriMatcher(), singleLine)));
        ontologyHeader.setOwlVersionIri(Utils.sanitize(get(extractor.getVersionIriMatcher(), singleLine)));
        ontologyHeader.setOwlVersionInfo(get(extractor.getVersionInfoMatcher(), singleLine));

        final List<String> imports = getMultiple(extractor.getImportsMatcher(), singleLine).stream().map(Utils::sanitize).collect(Collectors.toList());
        ontologyHeader.setOwlImports(imports);
        final List<String> unresolvable = new ArrayList<>();
        imports.stream().filter(i ->  fetchHeader(i) == null) .forEach(unresolvable::add);
        ontologyHeader.setNonResolvableImports(unresolvable);
        return ontologyHeader;
    }

    private OntologyHeader fetchHeader(final String iri) {
        try {
            return new HeaderFetcher().fetch(new URL(iri),8192);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
