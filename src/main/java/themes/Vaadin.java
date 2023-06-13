package themes;

import com.formdev.flatlaf.FlatLightLaf;

public class Vaadin extends FlatLightLaf {
  public static final String NAME = "Vaadin";

  public static boolean setup() {
    return setup(new Vaadin());
  }

  public static void installLafInfo() {
    installLafInfo(NAME, Vaadin.class);
  }

  @Override
  public String getName() {
    return NAME;
  }
}
