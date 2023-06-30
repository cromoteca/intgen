package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import java.awt.*;
import java.util.function.Supplier;
import javax.swing.*;
import net.datafaker.Faker;

public class TextField implements ComponentGenerator<JTextField> {
  private final int minWidth;
  private final int maxWidth;
  private final Faker faker = new Faker();
  private final Supplier<String>[] text =
      new Supplier[] {
        () -> faker.address().city(),
        () -> faker.address().country(),
        () -> faker.address().streetName(),
        () -> faker.address().streetAddress(),
        () -> faker.address().zipCode(),
        () -> faker.phoneNumber().cellPhone(),
        () -> faker.phoneNumber().phoneNumber(),
        () -> faker.internet().emailAddress(),
        () -> faker.internet().domainName(),
        () -> faker.internet().url(),
        () -> faker.name().fullName(),
        () -> faker.name().name(),
        () -> faker.name().username(),
        () -> faker.name().firstName(),
        () -> faker.name().lastName(),
        () -> faker.name().prefix(),
        () -> faker.name().suffix(),
        () -> "••••••••••••",
        () -> "●●●●●●●●●●●●"
      };

  public TextField() {
    this(75, 250);
  }

  public TextField(int minWidth, int maxWidth) {
    this.minWidth = minWidth;
    this.maxWidth = maxWidth;
  }

  @Override
  public JTextField generate() {
    var textField = new JTextField();
    textField.setPreferredSize(
        new Dimension(Randoms.nextInt(minWidth, maxWidth), textField.getPreferredSize().height));

    if (Randoms.nextBoolean()) {
      textField.setText(Randoms.pickOne(text).get());
    } else {
      textField.setText(Randoms.words(0, 6));
    }

    if (Randoms.nextDouble() > 0.8) {
      textField.setEditable(false);
    }

    return textField;
  }
}
