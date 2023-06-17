package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.Font;
import javax.swing.JLabel;

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
    var text = Intgen.words(1, 3);

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      text += " *";
    }

    var label = new JLabel(text, Intgen.RANDOM.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT);
    var applyBold = bold == null ? Intgen.RANDOM.nextDouble() > 0.7 : bold;

    if (applyBold) {
      label.setFont(label.getFont().deriveFont(Font.BOLD));
    }

    return label;
  }
}
