package cz.sio2.obo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestOntologyHeader {

    @ParameterizedTest
    @CsvFileSource(resources = "/version-testcases.csv", numLinesToSkip = 1, delimiter = ',')
    public void testGetOboVersionReturnsVersionIriIfPresent(String ontologyIri, String versionIri, String versionInfo, String computedVersionIri, String computedVersion) {
        final OntologyHeader header = new OntologyHeader();
        header.setOwlOntologyIri(ontologyIri);
        header.setOwlVersionIri(versionIri);
        header.setOwlVersionInfo(versionInfo);
        Assertions.assertEquals(computedVersion, header.getVersion());
        Assertions.assertEquals(computedVersionIri, header.getOboVersionIri());
    }
}
