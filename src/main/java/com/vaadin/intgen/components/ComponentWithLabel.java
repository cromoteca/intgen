package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.BorderLayout;
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
        var panel = new JPanel(new BorderLayout());
        var label = new JLabel(Intgen.words(1, 3));

        panel.add(label, labelPosition.getPosition());
        panel.add(field.generate(), BorderLayout.CENTER);

        return panel;
    }

    @Override
    public String getCategory() {
        return field.getCategory() + "With" + labelPosition.titleCaseName() + "Label";
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
