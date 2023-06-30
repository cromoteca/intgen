package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import com.vaadin.intgen.Randoms;
import java.awt.*;
import javax.swing.*;

public class LayoutGrid extends LayoutGenerator {

  @Override
  public JComponent generate() {
    var layout = new GridLayout(Randoms.nextInt(2, 5), Randoms.nextInt(3, 5));
    var panel = new JPanel();
    panel.setLayout(layout);
    return panel;
  }

  @Override
  public JComponent wrap(JComponent container) {
    var box = Box.createVerticalBox();
    box.add(container);
    return box;
  }
}
