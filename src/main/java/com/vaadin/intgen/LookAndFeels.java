package com.vaadin.intgen;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.*;
import themes.Vaadin;

public class LookAndFeels {
  private static final String[] flatThemes = new String[] {"Arc Dark", "Cobalt_2", "GitHub"};

  static {
    Stream.of(
            FlatDarculaLaf.class,
            FlatDarkLaf.class,
            FlatIntelliJLaf.class,
            FlatLightLaf.class,
            FlatMacLightLaf.class,
            FlatMacDarkLaf.class,
            Vaadin.class)
        .forEach(
            laf -> {
              UIManager.installLookAndFeel(laf.getSimpleName(), laf.getName());
            });
  }

  private final UIManager.LookAndFeelInfo[] installed;

  public LookAndFeels() {
    installed =
        Arrays.stream(UIManager.getInstalledLookAndFeels())
            .filter(laf -> !"CDE/Motif".equals(laf.getName()))
            .toArray(UIManager.LookAndFeelInfo[]::new);
  }

  public void pickOne() {
    var lafNumber = Randoms.nextInt(installed.length + flatThemes.length);
    if (lafNumber < installed.length) {
      var lookAndFeelClassName = installed[lafNumber].getClassName();
      try {
        UIManager.setLookAndFeel(lookAndFeelClassName);
      } catch (ClassNotFoundException
          | InstantiationException
          | IllegalAccessException
          | UnsupportedLookAndFeelException e) {
        throw new RuntimeException(e);
      }
    } else {
      var themeName = flatThemes[lafNumber - installed.length];
      IntelliJTheme.setup(Intgen.class.getResourceAsStream("/themes/" + themeName + ".theme.json"));
    }
  }
}
