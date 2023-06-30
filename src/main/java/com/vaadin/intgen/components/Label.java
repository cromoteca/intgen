package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import java.awt.*;
import javax.swing.*;

public class Label implements ComponentGenerator<JLabel> {
  private final Boolean bold;

  public Label() {
    this(null);
  }

  public Label(Boolean bold) {
    this.bold = bold;
  }

  @Override
  public JLabel generate() {
    var text = Randoms.words(1, 3);

    if (Randoms.nextDouble() > 0.8) {
      text += " *";
    }

    var label = new JLabel(text, Randoms.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT);
    var applyBold = bold == null ? Randoms.nextDouble() > 0.7 : bold;

    if (applyBold) {
      label.setFont(label.getFont().deriveFont(Font.BOLD));
    }

    return label;
  }
}
