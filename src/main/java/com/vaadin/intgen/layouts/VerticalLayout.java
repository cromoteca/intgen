package com.vaadin.intgen.layouts;

import com.vaadin.intgen.LayoutGenerator;
import javax.swing.Box;
import javax.swing.JComponent;

public class VerticalLayout extends LayoutGenerator {

    @Override
    public Box generate() {
        return Box.createVerticalBox();
    }

    @Override
    public JComponent generateWrapper(JComponent container) {
        var wrapper = Box.createVerticalBox();
        wrapper.add(container);
        wrapper.add(Box.createVerticalGlue());
        return wrapper;
    }

    @Override
    public boolean forbid(String parentCategory) {
        return getCategory().equals(parentCategory);
    }

}
