package cz.sio2.ontology.version.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.List;

@Getter
@Setter
@Slf4j
public class Configuration {

  private String name;

  private URL ontologyCatalogueIri;

  private String ontologyIriQuery;

  private int headerSize;

  private String versionFromVersionInfoRegex;

  private String versionFromVersionIriRegex;

  private List<VersionType> versionTypes;

}
