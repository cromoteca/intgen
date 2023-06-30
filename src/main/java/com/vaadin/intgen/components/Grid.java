package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Randoms;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Grid implements ComponentGenerator<JScrollPane> {

  @Override
  public JScrollPane generate() {
    var columnCount = Randoms.nextInt(2, 7);
    var rowCount = Randoms.nextInt(1, 6);

    var columnNames = new Vector<String>();

    for (var i = 0; i < columnCount; i++) {
      columnNames.add(Randoms.words(1, 3));
    }

    var data = new Vector<Vector<String>>();

    for (int i = 0; i < rowCount; i++) {
      var row = new Vector<String>();

      for (var j = 0; j < columnCount; j++) {
        row.add(Randoms.words(1, 3));
      }

      data.add(row);
    }

    var model = new DefaultTableModel(data, columnNames);
    var table = new JTable(model);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    for (var i = 0; i < table.getColumnCount(); i++) {
      var column = table.getColumnModel().getColumn(i);
      var width = Randoms.nextInt(50, 120);
      column.setPreferredWidth(width);
    }

    var selected = Randoms.nextInt(-1, rowCount);

    if (selected >= 0) {
      table.setRowSelectionInterval(selected, selected);
    }

    return new JScrollPane(table);
  }
}
