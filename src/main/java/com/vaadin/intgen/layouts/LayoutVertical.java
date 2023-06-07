package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import javax.swing.Box;
import javax.swing.JComponent;

public class LayoutVertical extends LayoutGenerator {

  @Override
  public Box generate() {
    return Box.createVerticalBox();
  }

  @Override
  public JComponent generateWrapper(JComponent container) {
    var box = Box.createVerticalBox();
    box.add(container);
    box.add(Box.createVerticalGlue());
    return box;
  }
}
