package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.awt.Component;
import javax.swing.JComboBox;

public class ComboBox implements ComponentGenerator {

    @Override
    public Component generate() {
        return new JComboBox<>(new String[]{Intgen.words(1, 4)});
    }

}
