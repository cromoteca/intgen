package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import java.awt.*;
import javax.swing.*;

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
    var icon = Randoms.nextDouble() > 0.8 ? Randoms.icon() : null;
    var text = icon == null || Randoms.nextDouble() > 0.5 ? Randoms.words(1, 2) : null;
    var button = new JButton(text, icon);
    button.setEnabled(Randoms.nextDouble() > 0.2);

    var size = button.getPreferredSize();
    var wFactor = Randoms.nextDouble() * (maxFactor - 1.0) + 1.0;
    var hFactor = Randoms.nextDouble() * (maxFactor - 1.0) + 1.0;
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
