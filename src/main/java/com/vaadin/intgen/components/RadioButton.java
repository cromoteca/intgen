package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import javax.swing.*;

public class RadioButton implements ComponentGenerator<JRadioButton> {

  @Override
  public JRadioButton generate() {
    return new JRadioButton(Randoms.words(1, 4));
  }
}
