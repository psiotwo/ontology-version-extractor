package cz.sio2.obo.extractor;

import cz.sio2.obo.Extractor;
import cz.sio2.obo.HeaderFetcher;
import cz.sio2.obo.OntologyHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestXMLOntologyHeaderExtractor {

    @ParameterizedTest
    @CsvFileSource(resources = "/xml-testcases.csv", numLinesToSkip = 1, delimiter = ',')
    public void testExtractVersionIriFromXMLExtractsVersionInfoCorrectlyIfPresent(
            String file,
            String ontologyIri,
            String versionIri,
            String versionInfo,
            String importsList) throws URISyntaxException, IOException {
        final List<String> imports = importsList != null ? Arrays.asList(importsList.split("\\|")) : Collections.emptyList();
        final String s = Files.readString(Paths.get(Objects.requireNonNull(getClass().getResource("/xml-testcases/" + file)).toURI()));
        final OntologyHeader header = new Extractor().extract(s, HeaderFetcher.ONTOLOGY_HEADER_EXTRACTORS.get("XMLOntologyHeaderExtractor"));
        Assertions.assertEquals(ontologyIri, header.getOwlOntologyIri());
        Assertions.assertEquals(versionIri, header.getOwlVersionIri());
        Assertions.assertEquals(versionInfo, header.getOwlVersionInfo());
        Assertions.assertEquals(imports, header.getOwlImports());
    }
}
