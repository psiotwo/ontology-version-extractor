package cz.sio2.ontology.version.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Utils {

  public static Configuration loadConfiguration(URL configurationUrl) {
    final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try (final InputStream is = configurationUrl.openStream()) {
      return mapper.readValue(is, new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
