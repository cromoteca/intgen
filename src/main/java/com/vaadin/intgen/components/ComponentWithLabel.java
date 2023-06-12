package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComponentWithLabel implements ComponentGenerator<JPanel> {

  private final ComponentGenerator<?> field;
  private final LabelPosition labelPosition;

  public ComponentWithLabel(ComponentGenerator<?> field, LabelPosition labelPosition) {
    this.field = field;
    this.labelPosition = labelPosition;
  }

  @Override
  public JPanel generate() {
    var borderLayout = new BorderLayout();
    borderLayout.setHgap(Intgen.RANDOM.nextInt(10));
    borderLayout.setVgap(Intgen.RANDOM.nextInt(10));
    var panel = new JPanel(borderLayout);
    var text = Intgen.words(1, 3);

    if (Intgen.RANDOM.nextDouble() > 0.8) {
      text += " *";
    }

    var label = new JLabel(text, Intgen.RANDOM.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT);

    if (labelPosition == LabelPosition.LEFT) {
      sizeLabel(label);
    }

    panel.add(label, labelPosition.getPosition());
    panel.add(field.generate(), BorderLayout.CENTER);

    return panel;
  }

  @Override
  public String getCategory() {
    return field.getCategory() + "With" + labelPosition.titleCaseName() + "Label";
  }

  protected void sizeLabel(JLabel label) {
    label.setPreferredSize(
        new Dimension(
            label.getPreferredSize().width + Intgen.RANDOM.nextInt(100),
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
      return name().substring(0, 1) + name().substring(1).toLowerCase();
    }
  }
}
