package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import javax.swing.*;

public class Checkbox implements ComponentGenerator<JCheckBox> {

  @Override
  public JCheckBox generate() {
    return new JCheckBox(Randoms.words(2, 6), Randoms.nextBoolean());
  }
}
