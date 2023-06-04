package com.vaadin.intgen.layouts;

import com.vaadin.intgen.Intgen;
import com.vaadin.intgen.LayoutGenerator;
import java.awt.Color;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class TabLayout extends LayoutGenerator {

    private static final HorizontalLayout HORIZONTAL_LAYOUT = new HorizontalLayout();
    private static final VerticalLayout VERTICAL_LAYOUT = new VerticalLayout();

    @Override
    public JComponent generate() {
        return (Intgen.RANDOM.nextBoolean() ? HORIZONTAL_LAYOUT : VERTICAL_LAYOUT).generate();
    }

    @Override
    public JTabbedPane generateWrapper(JComponent container) {
        var wrapper = new JTabbedPane();
        var tabCount = Intgen.RANDOM.nextInt(2, 6);
        var selectedTab = Intgen.RANDOM.nextInt(tabCount);

        for (int i = 0; i < tabCount; i++) {
            var component = i == selectedTab
                    ? container
                    : new JPanel();
            wrapper.addTab(Intgen.words(1, 3), component);
        }

        wrapper.setSelectedIndex(selectedTab);
        return wrapper;
    }

    @Override
    public boolean forbid(String parentCategory) {
        return VERTICAL_LAYOUT.getCategory().equals(parentCategory);
    }
}
