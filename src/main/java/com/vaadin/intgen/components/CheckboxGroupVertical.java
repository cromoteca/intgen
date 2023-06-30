package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import com.vaadin.intgen.layouts.LayoutVertical;
import javax.swing.*;

public class CheckboxGroupVertical implements ComponentGenerator<Box> {
  private final Checkbox generator = new Checkbox();
  private static final LayoutVertical layout = new LayoutVertical();

  @Override
  public Box generate() {
    var box = layout.generate();
    var count = Randoms.nextInt(2, 5);

    for (int i = 0; i < count; i++) {
      generator.add(box);
    }

    return box;
  }
}
