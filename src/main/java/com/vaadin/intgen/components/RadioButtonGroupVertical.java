package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import com.vaadin.intgen.layouts.LayoutVertical;
import javax.swing.*;

public class RadioButtonGroupVertical implements ComponentGenerator<Box> {
  private static final RadioButton radioButton = new RadioButton();
  private static final LayoutVertical layout = new LayoutVertical();

  @Override
  public Box generate() {
    var box = layout.generate();
    var count = Randoms.nextInt(2, 5);
    var selected = Randoms.nextInt(-1, count);

    for (int i = 0; i < count; i++) {
      radioButton.add(box);
      var radio = (JRadioButton) box.getComponent(i);
      radio.setSelected(i == selected);
      radio.setMaximumSize(radio.getPreferredSize());
    }

    return box;
  }
}
