package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TextAreaWithTopLabel implements ComponentGenerator<JPanel> {

    @Override
    public JPanel generate() {
        var panel = new JPanel(new BorderLayout());
        var label = new JLabel(Intgen.words(1, 3));
        var textArea = new JTextArea();
        var w = Intgen.RANDOM.nextInt(100, 200);
        var h = Intgen.RANDOM.nextInt(30, Math.min(w - 40, 100));
        textArea.setPreferredSize(new Dimension(w, h));
        textArea.setText(Intgen.words(0, 10));

        panel.add(label, BorderLayout.NORTH);
        panel.add(textArea, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public boolean forbid(String parentCategory) {
        return "HorizontalLayout".equals(parentCategory);
    }

}
