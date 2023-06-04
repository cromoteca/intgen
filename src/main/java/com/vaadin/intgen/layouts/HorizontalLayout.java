package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import java.awt.Color;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class HorizontalLayout extends LayoutGenerator {

    @Override
    public Box generate() {
        return Box.createHorizontalBox();
    }

    @Override
    public JComponent generateWrapper(JComponent container) {
        var wrapper = Box.createHorizontalBox();
        wrapper.add(container);
        wrapper.add(Box.createHorizontalGlue());
        return wrapper;
    }

    @Override
    public boolean forbid(String parentCategory) {
        return getCategory().equals(parentCategory);
    }

}
