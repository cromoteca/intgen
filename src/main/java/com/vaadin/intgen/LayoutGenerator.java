package com.vaadin.intgen;

import java.awt.*;
import javax.swing.*;

public abstract class LayoutGenerator implements ComponentGenerator<JComponent> {

  @Override
  public Added<JComponent> add(Container parent) {
    var container = generate();
    var wrapper = wrap(container);

    if (Randoms.nextDouble() > 0.8) {
      wrapper.setBorder(BorderFactory.createTitledBorder(Randoms.words(1, 3)));
    } else {
      var padding = Randoms.nextInt(10);
      wrapper.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }

    container.setName(getCategory());
    parent.add(wrapper, Randoms.nextInt(-1, parent.getComponentCount()));
    return new Added<>(wrapper, container);
  }

  public JComponent wrap(JComponent container) {
    return container;
  }

  @Override
  public boolean forbid(String parentCategory) {
    return getCategory().equals(parentCategory);
  }
}
