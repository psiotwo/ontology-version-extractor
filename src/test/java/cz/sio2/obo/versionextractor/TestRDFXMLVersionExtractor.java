package cz.sio2.obo.versionextractor;

import cz.sio2.obo.Version;
import cz.sio2.obo.versionextractor.RDFXMLVersionExtractor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TestRDFXMLVersionExtractor {

    @ParameterizedTest
    @CsvFileSource(resources = "/rdf-testcases.csv", numLinesToSkip = 1, delimiter = ',')
    public void testExtractVersionFromVersionIriFromRDFXMLWorksCorrectly(String file,
                                                                         String ontologyIri,
                                                                         String versionIri,
                                                                         String versionInfo) throws URISyntaxException, IOException {
        final String s = Files.readString(Paths.get(Objects.requireNonNull(getClass().getResource("/rdf-testcases/" + file)).toURI()));
        final Version version = new Version();
        new RDFXMLVersionExtractor().extract(s, version);
        Assertions.assertEquals(ontologyIri, version.getOwlOntologyIri());
        Assertions.assertEquals(versionIri, version.getOwlVersionIri());
        Assertions.assertEquals(versionInfo, version.getOwlVersionInfo());
    }
}
