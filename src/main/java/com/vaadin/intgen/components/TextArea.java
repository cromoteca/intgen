package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.Dimension;
import javax.swing.JTextArea;

public class TextArea implements ComponentGenerator<JTextArea> {

    @Override
    public JTextArea generate() {
        var textArea = new JTextArea();
        var w = Intgen.RANDOM.nextInt(100, 200);
        var h = Intgen.RANDOM.nextInt(30, Math.min(w - 40, 100));
        textArea.setPreferredSize(new Dimension(w, h));
        textArea.setText(Intgen.wordLines(0, 4, 6));

        return textArea;
    }

}
