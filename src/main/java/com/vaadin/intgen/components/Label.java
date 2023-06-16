package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JLabel;

public class Label implements ComponentGenerator<JLabel> {

  @Override
  public JLabel generate() {
    var text = Intgen.words(1, 3);

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      text += " *";
    }

    return new JLabel(text, Intgen.RANDOM.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT);
  }
}
