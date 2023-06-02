package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldWithLeftLabel implements ComponentGenerator {

    @Override
    public JPanel generate() {
        var panel = new JPanel(new BorderLayout());
        var label = new JLabel(Intgen.words(1, 3));
        var textField = new JTextField();
        textField.setPreferredSize(new Dimension(100, textField.getPreferredSize().height));

        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

}
