package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.Component;

import javax.swing.JCheckBox;

public class Checkbox implements ComponentGenerator {

    @Override
    public Component generate() {
        return new JCheckBox(Intgen.words(2, 6), Intgen.RANDOM.nextBoolean());
    }

}
