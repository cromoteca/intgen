package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.Box;
import javax.swing.JRadioButton;

public class HorizontalRadioButtons implements ComponentGenerator<Box> {

  @Override
  public Box generate() {
    var box = Box.createHorizontalBox();
    var count = Intgen.RANDOM.nextInt(2, 3);
    var selected = Intgen.RANDOM.nextInt(-1, count);

    for (int i = 0; i < count; i++) {
      var radio = new JRadioButton(Intgen.words(1, 4), i == selected);
      radio.setMaximumSize(radio.getPreferredSize());
      box.add(radio);
    }

    return box;
  }
}
