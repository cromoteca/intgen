package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JCheckBox;

public class Checkbox implements ComponentGenerator<JCheckBox> {

  @Override
  public JCheckBox generate() {
    return new JCheckBox(Intgen.words(2, 6), Intgen.RANDOM.nextBoolean());
  }
}
