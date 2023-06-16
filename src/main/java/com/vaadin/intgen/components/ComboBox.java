package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JComboBox;

public class ComboBox implements ComponentGenerator<JComboBox> {

  @Override
  public JComboBox generate() {
    var combo = new JComboBox<>(new String[] {Intgen.words(1, 4)});

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      combo.setSelectedIndex(0);
    }

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      combo.setEditable(false);
    }

    return combo;
  }
}
