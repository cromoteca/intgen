package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class VerticalLayout extends LayoutGenerator {

    @Override
    public Container _generate() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    @Override
    public boolean forbid(String parentCategory) {
        return getCategory().equals(parentCategory);
    }

}
