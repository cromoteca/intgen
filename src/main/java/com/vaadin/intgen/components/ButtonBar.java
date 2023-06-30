package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import com.vaadin.intgen.layouts.LayoutHorizontal;
import javax.swing.*;

public class ButtonBar implements ComponentGenerator<JComponent> {

  private final ComponentGenerator<JComponent> layoutGenerator = new LayoutHorizontal();
  private final Button buttonGenerator = new Button();

  @Override
  public JComponent generate() {
    var count = Randoms.nextInt(2, 6);
    var separatorIndex = Randoms.nextInt(count);
    var padding = Randoms.nextInt(0, 10);
    var separatorWidth = Randoms.nextInt(50, 150);

    var layout = layoutGenerator.generate();

    for (int i = 0; i < count; i++) {
      var button = buttonGenerator.generate();
      button.setName(buttonGenerator.getCategory());
      layout.add(button);

      if (i < count - 1) {
        layout.add(Box.createHorizontalStrut(i == separatorIndex ? separatorWidth : padding));
      }
    }

    return layout;
  }

  @Override
  public String getCategory() {
    return layoutGenerator.getCategory();
  }

  @Override
  public boolean forbid(String parentCategory) {
    return layoutGenerator.getCategory().equals(parentCategory);
  }
}
