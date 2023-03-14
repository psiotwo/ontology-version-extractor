package cz.sio2.obo.extractor;

import java.util.regex.Pattern;

public interface OntologyHeaderExtractor {

    Pattern getFormatMatcher();

    Pattern getIriMatcher();

    Pattern getVersionIriMatcher();

    Pattern getVersionInfoMatcher();

}
