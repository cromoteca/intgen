package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import javax.swing.Box;
import javax.swing.JComponent;

public class HorizontalLayout extends LayoutGenerator {

  @Override
  public Box generate() {
    return Box.createHorizontalBox();
  }

  @Override
  public JComponent generateWrapper(JComponent container) {
    var box = Box.createHorizontalBox();
    box.add(container);
    box.add(Box.createHorizontalGlue());
    return box;
  }

  @Override
  public boolean forbid(String parentCategory) {
    return getCategory().equals(parentCategory);
  }
}
