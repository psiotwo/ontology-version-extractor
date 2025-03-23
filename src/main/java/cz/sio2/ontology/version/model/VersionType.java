package cz.sio2.ontology.version.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
@Accessors(chain = true)
public class VersionType {

  private String name;

  private String description;

  private String color;

  private String versionIriPattern;

  private static final VersionType NO_VERSION_IRI_BUT_VERSIONINFO = new VersionType()
          .setName("Version Info Only")
          .setDescription("\"No/invalid owl:versionIri present, only owl:versionInfo.")
          .setColor("orange");

  private static final VersionType NO_VERSION_INFORMATION = new VersionType()
          .setName("No Version")
          .setDescription("Neither owl:versionIri nor owl:versionInfo present.")
          .setColor("deep-orange");

  public static final VersionType UNKNOWN = new VersionType()
          .setName("Unknown")
          .setDescription("A problem (e.g. failing connection) occurred when fetching ontology header.")
          .setColor("red");

  public static VersionType get(final String ontologyIri, final String versionIri, final String versionInfo, final Configuration configuration) {
    if (versionIri != null) {
      for (final VersionType type : configuration.getVersionTypes()) {
        if (type.getVersionIriPattern() != null && versionIri.matches(type.getVersionIriPattern())) {
          return type;
        }
      }
    }

    if (ontologyIri != null && versionInfo != null && !versionInfo.isEmpty()) {
      return NO_VERSION_IRI_BUT_VERSIONINFO;
    } else if (ontologyIri != null) {
      return NO_VERSION_INFORMATION;
    } else {
      return UNKNOWN;
    }
  }

  public static List<VersionType> getAll(final Configuration configuration) {
    final List<VersionType> versionTypes = new ArrayList<>(configuration.getVersionTypes());
    versionTypes.add(NO_VERSION_IRI_BUT_VERSIONINFO);
    versionTypes.add(NO_VERSION_INFORMATION);
    versionTypes.add(UNKNOWN);
    return versionTypes;
  }
}
