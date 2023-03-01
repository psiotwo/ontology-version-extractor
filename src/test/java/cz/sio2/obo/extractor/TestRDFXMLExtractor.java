package cz.sio2.obo.extractor;

import cz.sio2.obo.OntologyHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TestRDFXMLExtractor {

    @ParameterizedTest
    @CsvFileSource(resources = "/rdf-testcases.csv", numLinesToSkip = 1, delimiter = ',')
    public void testExtractVersionFromVersionIriFromRDFXMLWorksCorrectly(String file,
                                                                         String ontologyIri,
                                                                         String versionIri,
                                                                         String versionInfo) throws URISyntaxException, IOException {
        final String s = Files.readString(Paths.get(Objects.requireNonNull(getClass().getResource("/rdf-testcases/" + file)).toURI()));
        final OntologyHeader header = new OntologyHeader();
        new RDFXMLExtractor().extract(s, header);
        Assertions.assertEquals(ontologyIri, header.getOwlOntologyIri());
        Assertions.assertEquals(versionIri, header.getOwlVersionIri());
        Assertions.assertEquals(versionInfo, header.getOwlVersionInfo());
    }
}
