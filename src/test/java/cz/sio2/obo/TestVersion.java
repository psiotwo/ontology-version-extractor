package cz.sio2.obo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestVersion {

    @ParameterizedTest
    @CsvFileSource(resources = "/version-testcases.csv", numLinesToSkip = 1, delimiter = ',')
    public void testGetOboVersionReturnsVersionIriIfPresent(String ontologyIri, String versionIri, String versionInfo, String generatedVersionIri) {
        final Version version = new Version();
        version.setOwlOntologyIri(ontologyIri);
        version.setOwlVersionIri(versionIri);
        version.setOwlVersionInfo(versionInfo);
        Assertions.assertEquals(generatedVersionIri, version.getOboVersion());
    }
}
