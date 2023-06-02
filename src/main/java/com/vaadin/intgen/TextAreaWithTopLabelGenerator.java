package com.vaadin.intgen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TextAreaWithTopLabelGenerator implements ComponentGenerator {

    @Override
    public Component generate() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("LABEL");
        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(400, 200));

        panel.add(label, BorderLayout.NORTH);
        panel.add(textArea, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public String getCategory() {
        return "TextAreaWithTopLabel";
    }

}
