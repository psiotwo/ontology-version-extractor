package cz.sio2.obo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class TestVersionType {

    @ParameterizedTest
    @CsvFileSource(resources = "/version-type-testcases.csv", numLinesToSkip = 1, delimiter = ',')
    public void test( final String type, final String ontologyIri, final String versionIri, final String versionInfo ) {
        final VersionType expected = VersionType.valueOf(type);
        Assertions.assertEquals( expected,  VersionType.get(ontologyIri, versionIri, versionInfo));
    }
}
