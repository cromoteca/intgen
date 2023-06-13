package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JRadioButton;

public class RadioButton implements ComponentGenerator<JRadioButton> {

  @Override
  public JRadioButton generate() {
    return new JRadioButton(Intgen.words(1, 4));
  }
}
