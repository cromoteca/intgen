package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import com.vaadin.intgen.layouts.LayoutHorizontal;
import com.vaadin.intgen.layouts.LayoutVertical;
import javax.swing.*;

public class Form implements ComponentGenerator<JComponent> {
  private final ComponentGenerator<JComponent> layoutGenerator = new LayoutVertical();
  private final FieldGroup fieldGroup = new FieldGroup();

  @Override
  public JComponent generate() {
    var rows = Randoms.nextInt(1, 3);
    var cols = Randoms.nextInt(2, 4);

    var main = layoutGenerator.generate();
    new TextFieldWithLeftLabel().add(main);

    for (int i = 0; i < rows; i++) {
      var formContainer = new LayoutHorizontal().add(main).addedContainer();

      for (int j = 0; j < cols; j++) {
        fieldGroup.add(formContainer);
      }
    }

    new ButtonBar().add(main);

    return main;
  }

  @Override
  public String getCategory() {
    return layoutGenerator.getCategory();
  }
}
