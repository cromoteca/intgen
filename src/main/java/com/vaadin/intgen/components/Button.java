package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JButton;

public class Button implements ComponentGenerator<JButton> {

  @Override
  public JButton generate() {
    var icon = Intgen.RANDOM.nextDouble() > 0.8 ? Intgen.icon() : null;
    var text = icon == null || Intgen.RANDOM.nextDouble() > 0.5 ? Intgen.words(1, 2) : null;
    return new JButton(text, icon);
  }

  // dummy generator
  public static class DefaultButton implements ComponentGenerator<JButton> {

    @Override
    public JButton generate() {
      return new JButton();
    }
  }
}
