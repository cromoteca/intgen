package com.vaadin.intgen.components;

public class TextFieldWithTopLabel extends ComponentWithLabel {

  public TextFieldWithTopLabel() {
    super(new TextField(), LabelPosition.TOP);
  }
}
