package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import com.vaadin.intgen.layouts.LayoutHorizontal;
import javax.swing.*;

public class CheckboxGroupHorizontal implements ComponentGenerator<Box> {
  private final Checkbox generator = new Checkbox();
  private static final LayoutHorizontal layout = new LayoutHorizontal();

  @Override
  public Box generate() {
    var box = layout.generate();
    var count = Randoms.nextInt(2, 4);

    for (int i = 0; i < count; i++) {
      generator.add(box);
    }

    return box;
  }
}
