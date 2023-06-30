package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import javax.swing.*;

public class LayoutVertical extends LayoutGenerator {

  @Override
  public Box generate() {
    return Box.createVerticalBox();
  }

  @Override
  public JComponent wrap(JComponent container) {
    var box = Box.createVerticalBox();
    box.add(container);
    box.add(Box.createVerticalGlue());
    return box;
  }
}
