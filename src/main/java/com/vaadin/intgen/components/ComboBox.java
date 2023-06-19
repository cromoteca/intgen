package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JComboBox;

public class ComboBox implements ComponentGenerator<JComboBox> {
  private final int minWidth;
  private final int maxWidth;

  public ComboBox() {
    this(100, 250);
  }

  public ComboBox(int minWidth, int maxWidth) {
    this.minWidth = minWidth;
    this.maxWidth = maxWidth;
  }

  @Override
  public JComboBox generate() {
    var combo = new JComboBox<>(new String[] {Intgen.words(0, 6)});
    combo.setPreferredSize(
        new java.awt.Dimension(
            Intgen.RANDOM.nextInt(minWidth, maxWidth), combo.getPreferredSize().height));

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      combo.setSelectedIndex(0);
    }

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      combo.setEditable(false);
    }

    return combo;
  }
}
