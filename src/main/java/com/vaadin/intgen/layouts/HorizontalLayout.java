package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class HorizontalLayout implements LayoutGenerator {

    @Override
    public Container generate() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        return panel;
    }

}
