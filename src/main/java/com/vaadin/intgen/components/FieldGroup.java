package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import com.vaadin.intgen.layouts.LayoutVertical;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FieldGroup implements ComponentGenerator<JComponent> {

  private final int labelWidth = Intgen.RANDOM.nextInt(200, 300);
  private final int fieldWidth = Intgen.RANDOM.nextInt(200, 300);
  private final int hGap = Intgen.RANDOM.nextInt(10);

  private final ComponentGenerator<JComponent> layoutGenerator = new LayoutVertical();
  private final ComponentGenerator[] generators =
      new ComponentGenerator[] {
        new TextFieldWithLeftLabel(),
        new TextFieldWithLeftLabel(),
        new TextFieldWithLeftLabel(),
        new TextFieldWithLeftLabel(),
        new ComboBoxWithLeftLabel(),
        new RadioButtonGroupHorizontal(),
      };

  @Override
  public JComponent generate() {
    var rows = Intgen.RANDOM.nextInt(3, 8);
    var labelAlignment = Intgen.RANDOM.nextBoolean() ? JLabel.LEFT : JLabel.RIGHT;
    var box = layoutGenerator.generate();

    for (int i = 0; i < rows; i++) {
      Intgen.pickOne(generators).add(box);
    }

    for (int i = 0; i < rows; i++) {
      var generated = (JComponent) box.getComponent(i);

      if (generated.getComponentCount() == 2 && generated instanceof JPanel jPanel) {
        ((BorderLayout) jPanel.getLayout()).setHgap(hGap);
        var label = generated.getComponent(0);

        if (label instanceof JLabel jLabel) {
          jLabel.setPreferredSize(new Dimension(labelWidth, label.getPreferredSize().height));
          jLabel.setHorizontalAlignment(labelAlignment);

          var field = generated.getComponent(1);
          field.setPreferredSize(new Dimension(fieldWidth, field.getPreferredSize().height));
        }
      }
    }

    return box;
  }

  @Override
  public String getCategory() {
    return layoutGenerator.getCategory();
  }
}
