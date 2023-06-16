package com.vaadin.intgen;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;

public interface ComponentGenerator<T extends Component> {

  default Added<T> add(Container parent) {
    var component = generate();
    component.setMaximumSize(component.getPreferredSize());
    component.setName(getCategory());
    ((JComponent) component).setAlignmentX(0);
    parent.add(component, Intgen.RANDOM.nextInt(-1, parent.getComponentCount()));
    return new Added<>(component, null);
  }

  T generate();

  default String getCategory() {
    return getClass().getSimpleName();
  }

  default boolean forbid(String parentCategory) {
    return false;
  }

  public record Added<T>(T added, JComponent addedContainer) {}
}
