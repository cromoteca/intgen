package com.vaadin.intgen.layouts;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import com.vaadin.intgen.LayoutGenerator;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class TabLayout extends LayoutGenerator {

    private static final HorizontalLayout HORIZONTAL_LAYOUT = new HorizontalLayout();
    private static final VerticalLayout VERTICAL_LAYOUT = new VerticalLayout();
    private static final Tab TAB = new Tab();
    private static final ActiveTab ACTIVE_TAB = new ActiveTab();

    @Override
    public JComponent generate() {
        return (Intgen.RANDOM.nextBoolean() ? HORIZONTAL_LAYOUT : VERTICAL_LAYOUT).generate();
    }

    @Override
    public JTabbedPane generateWrapper(JComponent container) {
        var tabbedPane = new JTabbedPane();
        var tabCount = Intgen.RANDOM.nextInt(2, 6);
        var selectedTab = Intgen.RANDOM.nextInt(tabCount);

        for (int i = 0; i < tabCount; i++) {
            var component = i == selectedTab ? container : new JPanel();
            tabbedPane.addTab(null, component);
            var generator = i == selectedTab ? ACTIVE_TAB : TAB;
            tabbedPane.setTabComponentAt(i, generator.generate());
        }

        tabbedPane.setSelectedIndex(selectedTab);
        return tabbedPane;
    }

    @Override
    public boolean forbid(String parentCategory) {
        return VERTICAL_LAYOUT.getCategory().equals(parentCategory);
    }

    public static class Tab implements ComponentGenerator<JLabel> {

        @Override
        public JLabel generate() {
            return new JLabel(Intgen.words(1, 2));
        }

    }

    public static class ActiveTab implements ComponentGenerator<JLabel> {

        @Override
        public JLabel generate() {
            return new JLabel(Intgen.words(1, 2));
        }

    }
}
