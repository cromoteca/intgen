package com.vaadin.intgen.layouts;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.LayoutGenerator;
import com.vaadin.intgen.Randoms;
import java.awt.*;
import javax.swing.*;

public class LayoutTabs extends LayoutGenerator {

  private static final LayoutHorizontal HORIZONTAL_LAYOUT = new LayoutHorizontal();
  private static final LayoutVertical VERTICAL_LAYOUT = new LayoutVertical();
  private static final Tab TAB = new Tab();
  private static final TabActive ACTIVE_TAB = new TabActive();

  @Override
  public JComponent generate() {
    return (Randoms.nextBoolean() ? HORIZONTAL_LAYOUT : VERTICAL_LAYOUT).generate();
  }

  @Override
  public JTabbedPane wrap(JComponent container) {
    var tabbedPane = new JTabbedPane();
    var tabCount = Randoms.nextInt(2, 6);
    var selectedTab = Randoms.nextInt(tabCount);

    for (int i = 0; i < tabCount; i++) {
      var component = i == selectedTab ? container : new JPanel();
      tabbedPane.addTab(null, component);
      var generator = i == selectedTab ? ACTIVE_TAB : TAB;
      tabbedPane.setTabComponentAt(i, generator.generate());
    }

    tabbedPane.setSelectedIndex(selectedTab);
    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    tabbedPane.setMinimumSize(
        new Dimension(
            Math.max(tabbedPane.getMinimumSize().width, 50 * tabCount),
            tabbedPane.getMinimumSize().height));
    return tabbedPane;
  }

  public static class Tab implements ComponentGenerator<JLabel> {

    @Override
    public JLabel generate() {
      return new JLabel(Randoms.words(1, 2));
    }
  }

  public static class TabActive implements ComponentGenerator<JLabel> {

    @Override
    public JLabel generate() {
      return new JLabel(Randoms.words(1, 2));
    }
  }
}
