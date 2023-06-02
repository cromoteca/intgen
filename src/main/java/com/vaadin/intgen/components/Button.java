package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JButton;

public class Button implements ComponentGenerator {

    private static final String[] BUTTON_TEXTS = {"Submit", "Cancel", "Reset", "Close", "Help"};

    @Override
    public JButton generate() {
        var buttonText = BUTTON_TEXTS[Intgen.RANDOM.nextInt(BUTTON_TEXTS.length)];
        var button = new JButton(buttonText);
        return button;
    }

    @Override
    public String getCategory() {
        return "Button";
    }

}
