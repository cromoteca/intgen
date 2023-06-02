package com.vaadin.intgen;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;

public class ComboBoxGenerator implements ComponentGenerator {

    private static final String[] LABELS = {
        "Male",
        "Female",
        "Other",
        "Yes",
        "No",
        "Small",
        "Medium",
        "Large",
        "Red",
        "Blue"
    };

    @Override
    public Component generate() {
        List<String> some = new ArrayList<>(Arrays.stream(LABELS).filter(s -> Intgen.RANDOM.nextBoolean()).toList());
        Collections.shuffle(some, Intgen.RANDOM);
        return new JComboBox<>(some.toArray(String[]::new));
    }

    @Override
    public String getCategory() {
        return "ComboBox";
    }

}
