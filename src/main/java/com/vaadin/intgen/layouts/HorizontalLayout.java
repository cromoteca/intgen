package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class HorizontalLayout extends LayoutGenerator {

    @Override
    public Container _generate() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        return panel;
    }

    @Override
    public boolean forbid(String parentCategory) {
        return getCategory().equals(parentCategory);
    }

}
