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
import java.util.ArrayList;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

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
    var labelAlignment = Intgen.RANDOM.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT;
    var rows = Intgen.RANDOM.nextInt(3, 8);
    var labels = new ArrayList<JLabel>(rows);
    var fields = new ArrayList<Component>(rows);
    var labelWidth = labels.stream().mapToInt(l -> l.getPreferredSize().width).max().orElse(200);
    var fieldWidth = fields.stream().mapToInt(l -> l.getPreferredSize().width).max().orElse(200);

    IntStream.range(0, rows)
        .forEach(
            i -> {
              var label = labelGenerator.generate();
              label.setName(labelGenerator.getCategory());
              label.setHorizontalAlignment(labelAlignment);
              labels.add(label);

              var fieldGenerator = Intgen.pickOne(generators);
              var field = fieldGenerator.generate();
              field.setName(fieldGenerator.getCategory());
              fields.add(field);
            });

    var panel = new JPanel(new GridBagLayout());

    switch (Intgen.RANDOM.nextInt(6)) {
      case 0 -> panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
      case 1 -> panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      case 2 -> panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      case 3 -> panel.setBorder(BorderFactory.createTitledBorder(Intgen.words(1, 4)));
    }

    var constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.WEST;
    constraints.insets = new Insets(5, 5, 5, 5);

    for (int i = 0; i < rows; i++) {
      var label = labels.get(i);
      label.setPreferredSize(new Dimension(labelWidth, label.getPreferredSize().height));
      addComponent(panel, label, constraints, 0, i);

      var field = fields.get(i);
      field.setPreferredSize(new Dimension(fieldWidth, field.getPreferredSize().height));
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
