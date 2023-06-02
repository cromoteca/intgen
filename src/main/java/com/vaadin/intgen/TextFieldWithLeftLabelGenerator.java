package com.vaadin.intgen;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldWithLeftLabelGenerator implements ComponentGenerator {

    private static final String[] LABEL_TEXTS = {"First name", "Last name", "Email", "City", "Country"};

    @Override
    public JPanel generate() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(LABEL_TEXTS[Intgen.RANDOM.nextInt(LABEL_TEXTS.length)]);
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, textField.getPreferredSize().height));

        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public String getCategory() {
        return "TextFieldWithLeftLabel";
    }

}
