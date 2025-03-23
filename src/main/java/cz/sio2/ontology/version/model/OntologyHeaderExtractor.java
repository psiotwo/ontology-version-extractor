package cz.sio2.ontology.version.model;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;


@Getter
public class OntologyHeaderExtractor {

    @Setter
    String name;

    Pattern formatMatcher;

    Pattern iriMatcher;

    Pattern versionIriMatcher;

    Pattern versionInfoMatcher;

    Pattern importsMatcher;

    public void setFormatMatcher(String formatMatcher) {
        this.formatMatcher = Pattern.compile(formatMatcher);
    }

    public void setIriMatcher(String iriMatcher) {
        this.iriMatcher = Pattern.compile(iriMatcher);
    }

    public void setVersionIriMatcher(String versionIriMatcher) {
        this.versionIriMatcher = Pattern.compile(versionIriMatcher);
    }

    public void setVersionInfoMatcher(String versionInfoMatcher) {
        this.versionInfoMatcher = Pattern.compile(versionInfoMatcher);
    }

    public void setImportsMatcher(String importsMatcher) {
        this.importsMatcher = Pattern.compile(importsMatcher);
    }
}
