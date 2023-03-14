package cz.sio2.obo.extractor;

import cz.sio2.obo.Extractor;
import cz.sio2.obo.OntologyHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TestXMLOntologyHeaderExtractor {

    @ParameterizedTest
    @CsvFileSource(resources = "/xml-testcases.csv", numLinesToSkip = 1, delimiter = ',')
    public void testExtractVersionIriFromXMLExtractsVersionInfoCorrectlyIfPresent(
            String file,
            String ontologyIri,
            String versionIri,
            String versionInfo) throws URISyntaxException, IOException {
        final String s = Files.readString(Paths.get(Objects.requireNonNull(getClass().getResource("/xml-testcases/" + file)).toURI()));
        final OntologyHeader header = new Extractor().extract(s, new XMLOntologyHeaderExtractor());
        Assertions.assertEquals(ontologyIri, header.getOwlOntologyIri());
        Assertions.assertEquals(versionIri, header.getOwlVersionIri());
        Assertions.assertEquals(versionInfo, header.getOwlVersionInfo());
    }
}
