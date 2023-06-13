package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import com.vaadin.intgen.layouts.LayoutHorizontal;
import javax.swing.Box;

public class ComboBoxGroupHorizontal implements ComponentGenerator<Box> {
  private final ComboBox generator = new ComboBox();
  private static final LayoutHorizontal layout = new LayoutHorizontal();

  @Override
  public Box generate() {
    var box = layout.generate();
    var count = Intgen.RANDOM.nextInt(2, 3);

    for (int i = 0; i < count; i++) {
      generator.add(box);
    }

    return box;
  }
}
