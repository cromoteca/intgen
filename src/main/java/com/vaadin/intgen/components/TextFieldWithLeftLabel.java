package com.vaadin.intgen.components;

public class TextFieldWithLeftLabel extends ComponentWithLabel {

  public TextFieldWithLeftLabel() {
    super(new TextField(), LabelPosition.LEFT);
  }
}
