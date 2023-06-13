package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JLabel;

public class FreeText implements ComponentGenerator<JLabel> {

  @Override
  public JLabel generate() {
    var text =
        String.format(
            "<html><body style='width: %dpx'>%s</body></html>",
            Intgen.RANDOM.nextInt(100, 250), Intgen.words(4, 15));
    var label = new JLabel(text);
    return label;
  }
}
