package com.vaadin.intgen;

import javax.swing.JButton;

public class ButtonGenerator implements ComponentGenerator {
    private static final String[] BUTTON_TEXTS = { "Submit", "Cancel", "Reset", "Close", "Help" };

    @Override
    public JButton generate() {
        String buttonText = BUTTON_TEXTS[Intgen.RANDOM.nextInt(BUTTON_TEXTS.length)];
        JButton button = new JButton(buttonText);
        return button;
    }

    @Override
    public String getCategory() {
        return "Button";
    }
    
}
