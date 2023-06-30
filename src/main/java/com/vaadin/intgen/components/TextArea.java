package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import java.awt.*;
import javax.swing.*;

public class TextArea implements ComponentGenerator<JTextArea> {

  @Override
  public JTextArea generate() {
    var textArea = new JTextArea();
    var w = Randoms.nextInt(100, 200);
    var h = Randoms.nextInt(30, Math.min(w - 40, 100));
    textArea.setPreferredSize(new Dimension(w, h));
    textArea.setText(Randoms.wordLines(0, 4, 8));

    return textArea;
  }
}
