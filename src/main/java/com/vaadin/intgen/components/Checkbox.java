package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.Component;

import javax.swing.JCheckBox;

public class Checkbox implements ComponentGenerator {

    private static final String[] LABELS = {
        "I agree to the terms and conditions",
        "Subscribe to the newsletter",
        "Remember me",
        "Enable notifications",
        "Receive promotional offers",
        "Allow cookies",
        "Send me updates",
        "Receive special offers",
        "Participate in surveys",
        "Opt-in for marketing communications"
    };

    @Override
    public Component generate() {
        String text = LABELS[Intgen.RANDOM.nextInt(LABELS.length)];
        return new JCheckBox(text, Intgen.RANDOM.nextBoolean());
    }

    @Override
    public String getCategory() {
        return "Checkbox";
    }

}
