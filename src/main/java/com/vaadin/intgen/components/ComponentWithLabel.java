package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class ComponentWithLabel implements ComponentGenerator<JPanel> {

  private final ComponentGenerator<?> fieldGenerator;
  private final Label labelGenerator = new Label();
  private final LabelPosition labelPosition;

  public ComponentWithLabel(ComponentGenerator<?> fieldGenerator, LabelPosition labelPosition) {
    this.fieldGenerator = fieldGenerator;
    this.labelPosition = labelPosition;
  }

  @Override
  public JPanel generate() {
    var borderLayout = new BorderLayout();
    borderLayout.setHgap(Randoms.nextInt(10));
    borderLayout.setVgap(Randoms.nextInt(10));
    var panel = new JPanel(borderLayout);
    var label = labelGenerator.generate();
    label.setName(labelGenerator.getCategory());

    if (labelPosition == LabelPosition.LEFT) {
      sizeLabel(label);
    }

    panel.add(label, labelPosition.getPosition());
    var field = fieldGenerator.generate();
    field.setName(fieldGenerator.getCategory());
    panel.add(field, BorderLayout.CENTER);

    switch (Randoms.nextInt(20)) {
      case 0 -> panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
      case 1 -> panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      case 2 -> panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

    return panel;
  }

  @Override
  public String getCategory() {
    return fieldGenerator.getCategory() + "With" + labelPosition.titleCaseName() + "Label";
  }

  protected void sizeLabel(JLabel label) {
    label.setPreferredSize(
        new Dimension(
            label.getPreferredSize().width + Randoms.nextInt(100),
            label.getPreferredSize().height));
  }

  public enum LabelPosition {
    TOP(BorderLayout.NORTH),
    LEFT(BorderLayout.WEST);

    private final String position;

    private LabelPosition(String position) {
      this.position = position;
    }

    public String getPosition() {
      return position;
    }

    public String titleCaseName() {
      return name().charAt(0) + name().substring(1).toLowerCase();
    }
  }
}
