package com.vaadin.intgen.layouts;

import com.vaadin.intgen.Intgen;
import com.vaadin.intgen.LayoutGenerator;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class LayoutGrid extends LayoutGenerator {

  @Override
  public JComponent generate() {
    var layout = new GridLayout(Intgen.RANDOM.nextInt(3, 5), Intgen.RANDOM.nextInt(3, 5));
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
