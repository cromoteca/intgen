package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import javax.swing.JComboBox;

public class ComboBox implements ComponentGenerator<JComboBox> {

    @Override
    public JComboBox generate() {
        return new JComboBox<>(new String[]{Intgen.words(1, 4)});
    }

}