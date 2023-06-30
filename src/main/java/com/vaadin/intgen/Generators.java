package com.vaadin.intgen;

import com.vaadin.intgen.components.*;
import com.vaadin.intgen.layouts.LayoutGrid;
import com.vaadin.intgen.layouts.LayoutHorizontal;
import com.vaadin.intgen.layouts.LayoutTabs;
import com.vaadin.intgen.layouts.LayoutVertical;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generators {
  public static final List<LayoutGenerator> LAYOUTS =
      List.of(
          new LayoutGrid(),
          new LayoutHorizontal(),
          new LayoutHorizontal(),
          new LayoutHorizontal(),
          new LayoutTabs(),
          new LayoutVertical(),
          new LayoutVertical(),
          new LayoutVertical(),
          new LayoutVertical());
  public static final List<ComponentGenerator<?>> COMPONENTS =
      List.of(
          new Button(),
          new Button(2.0),
          new ButtonBar(),
          new Checkbox(),
          new CheckboxGroupHorizontal(),
          new CheckboxGroupVertical(),
          new ComboBox(40, 600),
          new ComboBoxGroupHorizontal(),
          new ComboBoxWithLeftLabel(),
          new ComboBoxWithTopLabel(),
          new FieldGroup(),
          new Form(),
          new FreeText(),
          new Grid(),
          new RadioButtonGroupHorizontal(),
          new RadioButtonGroupHorizontalWithLeftLabel(),
          new RadioButtonGroupVertical(),
          new TextAreaWithLeftLabel(),
          new TextAreaWithTopLabel(),
          new TextField(30, 600),
          new TextFieldWithLeftLabel(),
          new TextFieldWithTopLabel());
  public static final List<ComponentGenerator<?>> OTHER_COMPONENTS =
      List.of(
          new Button.DefaultButton(),
          new Label(),
          new LayoutTabs.TabActive(),
          new LayoutTabs.Tab(),
          new RadioButton(),
          new TextArea());
  public static final SortedSet<String> ALL =
      Stream.concat(LAYOUTS.stream(), Stream.concat(COMPONENTS.stream(), OTHER_COMPONENTS.stream()))
          .map(ComponentGenerator::getCategory)
          .filter(Objects::nonNull)
          .collect(Collectors.toCollection(TreeSet::new));

  public static final Map<String, Integer> CATEGORIES = new HashMap<>();

  static {
    var index = 0;
    for (var category : ALL) {
      CATEGORIES.put(category, index++);
    }
  }
}
