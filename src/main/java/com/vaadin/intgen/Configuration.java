package com.vaadin.intgen;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
  private static final Properties values = new Properties();

  static {
    try (var reader =
        new FileReader(new File(System.getProperty("user.dir"), "intgen.properties"))) {
      values.load(reader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static int intParam(String key) {
    return Integer.parseInt(values.getProperty(key));
  }

  public static boolean booleanParam(String key) {
    return Boolean.parseBoolean(values.getProperty(key));
  }

  public static File dataSet() {
    return new File(values.getProperty("dataSetLocation"));
  }

  public static String getProperty(String key) {
    return values.getProperty(key);
  }
}
