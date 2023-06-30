package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import com.vaadin.intgen.layouts.LayoutVertical;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class FieldGroup implements ComponentGenerator<JComponent> {

  // private final String forbiddenParent = new LayoutHorizontal().getCategory();
  private final ComponentGenerator<JComponent> layoutGenerator = new LayoutVertical();
  private final Label labelGenerator = new Label(Randoms.nextDouble() > 0.7);
  private final ComponentGenerator[] fieldGenerators =
      new ComponentGenerator[] {
        new TextField(),
        new TextField(),
        new TextField(),
        new ComboBox(),
        new RadioButtonGroupHorizontal(),
      };

  @Override
  public JComponent generate() {
    var labelAlignment = Randoms.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT;
    var rows = Randoms.nextInt(3, 8);
    var labels = new ArrayList<JLabel>(rows);
    var fields = new ArrayList<Component>(rows);

    IntStream.range(0, rows)
        .forEach(
            i -> {
              var label = labelGenerator.generate();
              label.setName(labelGenerator.getCategory());
              label.setHorizontalAlignment(labelAlignment);
              labels.add(label);

              var fieldGenerator = Randoms.pickOne(fieldGenerators);
              var field = fieldGenerator.generate();
              field.setName(fieldGenerator.getCategory());
              fields.add(field);
            });

    var labelWidth = labels.stream().mapToInt(l -> l.getPreferredSize().width).max().orElse(200);
    var fieldWidth = fields.stream().mapToInt(f -> f.getPreferredSize().width).max().orElse(200);

    var panel = layoutGenerator.generate();

    switch (Randoms.nextInt(6)) {
      case 0 -> panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
      case 1 -> panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      case 2 -> panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
      case 3 -> panel.setBorder(BorderFactory.createTitledBorder(Randoms.words(1, 4)));
    }

    var constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.WEST;
    constraints.insets = new Insets(5, 5, 5, 5);

    for (int i = 0; i < rows; i++) {
      var row = new JPanel(new GridBagLayout());

      var label = labels.get(i);
      label.setPreferredSize(new Dimension(labelWidth, label.getPreferredSize().height));
      addComponent(row, label, constraints, 0, i);

      var field = fields.get(i);
      field.setPreferredSize(new Dimension(fieldWidth, field.getPreferredSize().height));
      addComponent(row, field, constraints, 1, i);

      row.setName(field.getName() + "WithLeftLabel");
      panel.add(row);
    }

    return panel;
  }

  @Override
  public String getCategory() {
    return layoutGenerator.getCategory();
  }

  // @Override
  // public boolean forbid(String parentCategory) {
  //   return forbiddenParent.equals(parentCategory);
  // }

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
