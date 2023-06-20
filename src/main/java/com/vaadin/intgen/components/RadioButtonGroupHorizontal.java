package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import com.vaadin.intgen.layouts.LayoutHorizontal;
import javax.swing.Box;
import javax.swing.JRadioButton;

public class RadioButtonGroupHorizontal implements ComponentGenerator<Box> {
  private static final RadioButton radioButton = new RadioButton();
  private static final LayoutHorizontal layout = new LayoutHorizontal();

  @Override
  public Box generate() {
    var box = layout.generate();
    var count = Intgen.RANDOM.nextInt(2, 4);
    var selected = Intgen.RANDOM.nextInt(-1, count);

    for (int i = 0; i < count; i++) {
      radioButton.add(box);
      var radio = (JRadioButton) box.getComponent(i);
      radio.setSelected(i == selected);
      radio.setMaximumSize(radio.getPreferredSize());
    }

    return box;
  }
}
