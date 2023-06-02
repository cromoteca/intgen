package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JButton;

public class Button implements ComponentGenerator {

    @Override
    public JButton generate() {
        String buttonText;

        if (Intgen.RANDOM.nextBoolean()) {
            buttonText = Intgen.emoji() + ' ' + Intgen.words(0, 2);
        } else {
            buttonText = Intgen.words(1, 2);
        }

        return new JButton(buttonText);
    }

}
