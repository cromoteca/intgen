package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.Dimension;
import javax.swing.JButton;

public class Button implements ComponentGenerator<JButton> {
  private final double maxFactor;

  public Button() {
    this(1.0);
  }

  public Button(double maxFactor) {
    this.maxFactor = maxFactor;
  }

  @Override
  public JButton generate() {
    var icon = Intgen.RANDOM.nextDouble() > 0.8 ? Intgen.icon() : null;
    var text = icon == null || Intgen.RANDOM.nextDouble() > 0.5 ? Intgen.words(1, 2) : null;
    var button = new JButton(text, icon);
    button.setEnabled(Intgen.RANDOM.nextDouble() > 0.2);

    var size = button.getPreferredSize();
    var wFactor = Intgen.RANDOM.nextDouble() * (maxFactor - 1.0) + 1.0;
    var hFactor = Intgen.RANDOM.nextDouble() * (maxFactor - 1.0) + 1.0;
    button.setPreferredSize(
        new Dimension((int) (size.width * wFactor), (int) (size.height * hFactor)));

    return button;
  }

  // dummy generator
  public static class DefaultButton implements ComponentGenerator<JButton> {

    @Override
    public JButton generate() {
      return new JButton();
    }
  }
}
