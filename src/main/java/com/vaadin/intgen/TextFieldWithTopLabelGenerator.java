package com.vaadin.intgen;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldWithTopLabelGenerator implements ComponentGenerator {
    private static final String[] LABEL_TEXTS = {"First name", "Last name", "Email", "City", "Country"};

    @Override
    public JPanel generate() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(LABEL_TEXTS[Intgen.RANDOM.nextInt(LABEL_TEXTS.length)]);
        JTextField textField = new JTextField();

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public String getCategory() {
        return "TextFieldWithTopLabel";
    }
    
}
