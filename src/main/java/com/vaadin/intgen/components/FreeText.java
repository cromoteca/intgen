package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JLabel;

public class FreeText implements ComponentGenerator<JLabel> {

  public static final String[] TAGS = new String[] {"h1", "h2", "h3", "p", "p", "p", "p", "pre"};

  @Override
  public JLabel generate() {
    var tag = Intgen.pickOne(TAGS);
    var text =
        String.format(
            "<html><body style='width: %dpx'><%s>%s</%s></body></html>",
            Intgen.RANDOM.nextInt(100, 250), tag, Intgen.words(4, 15), tag);
    return new JLabel(text);
  }
}
