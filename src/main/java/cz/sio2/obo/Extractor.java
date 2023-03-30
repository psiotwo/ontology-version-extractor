package cz.sio2.obo;

import cz.sio2.obo.extractor.OntologyHeaderExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public OntologyHeader extract(final String s, final OntologyHeaderExtractor extractor) {
        final OntologyHeader ontologyHeader = new OntologyHeader();
        final String singleLine = s.replace('\n', ' ');
        if (!extractor.getFormatMatcher().matcher(singleLine).matches()) {
            return null;
        }
        ontologyHeader.setOwlOntologyIri(Utils.sanitize(get(extractor.getIriMatcher(), singleLine)));
        ontologyHeader.setOwlVersionIri(Utils.sanitize(get(extractor.getVersionIriMatcher(), singleLine)));
        ontologyHeader.setOwlVersionInfo(get(extractor.getVersionInfoMatcher(), singleLine));

        final List<String> imports = getMultiple(extractor.getImportsMatcher(), singleLine).stream().map(Utils::sanitize).collect(Collectors.toList());
        ontologyHeader.setOwlImports(imports);
        final List<String> nonresolvable = new ArrayList<>();
        imports.stream().filter(i ->  fetchHeader(i) == null) .forEach(nonresolvable::add);
        ontologyHeader.setNonResolvableImports(nonresolvable);
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
