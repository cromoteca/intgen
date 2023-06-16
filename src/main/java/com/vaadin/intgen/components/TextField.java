package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.Dimension;
import javax.swing.JTextField;

public class TextField implements ComponentGenerator<JTextField> {

  @Override
  public JTextField generate() {
    var textField = new JTextField();
    textField.setPreferredSize(new Dimension(100, textField.getPreferredSize().height));
    textField.setText(Intgen.words(0, 3));

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      textField.setEditable(false);
    }

    return textField;
  }
}
