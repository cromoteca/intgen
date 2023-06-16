package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import com.vaadin.intgen.layouts.LayoutVertical;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FieldGroup implements ComponentGenerator<JComponent> {

  private final ComponentGenerator<JComponent> layoutGenerator = new LayoutVertical();
  private final Label labelGenerator = new Label();
  private final ComponentGenerator[] generators =
      new ComponentGenerator[] {
        new TextField(),
        new TextField(),
        new TextField(),
        new ComboBox(),
        new RadioButtonGroupHorizontal(),
      };

  @Override
  public JComponent generate() {
    var labelWidth = Intgen.RANDOM.nextInt(200, 300);
    var fieldWidth = Intgen.RANDOM.nextInt(200, 300);
    // var hGap = Intgen.RANDOM.nextInt(10);
    var labelAlignment = Intgen.RANDOM.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT;
    var rows = Intgen.RANDOM.nextInt(3, 8);

    var panel = new JPanel(new GridBagLayout());
    var constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.WEST;
    constraints.insets = new Insets(5, 5, 5, 5);

    for (int i = 0; i < rows; i++) {
      var label = labelGenerator.generate();
      label.setName(labelGenerator.getCategory());
      label.setSize(new Dimension(labelWidth, label.getPreferredSize().height));
      label.setHorizontalAlignment(labelAlignment);
      var fieldGenerator = Intgen.pickOne(generators);
      var field = fieldGenerator.generate();
      field.setSize(new Dimension(fieldWidth, field.getPreferredSize().height));
      field.setName(fieldGenerator.getCategory());
      addComponent(panel, label, constraints, 0, i);
      addComponent(panel, field, constraints, 1, i);
    }

    return panel;
  }

  @Override
  public String getCategory() {
    return layoutGenerator.getCategory();
  }

  @Override
  public boolean forbid(String parentCategory) {
    return "LayoutHorizontal".equals(parentCategory);
  }

  private void addComponent(
      Container container,
      Component component,
      GridBagConstraints constraints,
      int gridx,
      int gridy) {
    constraints.gridx = gridx;
    constraints.gridy = gridy;
    container.add(component, constraints);
  }
}
