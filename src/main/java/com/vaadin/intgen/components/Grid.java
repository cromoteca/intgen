package com.vaadin.intgen.components;

import com.vaadin.intgen.ComponentGenerator;
import com.vaadin.intgen.Intgen;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Grid implements ComponentGenerator<JScrollPane> {

  @Override
  public JScrollPane generate() {
    var columnCount = Intgen.RANDOM.nextInt(2, 7);
    var rowCount = Intgen.RANDOM.nextInt(1, 6);

    var columnNames = new Vector<String>();

    for (var i = 0; i < columnCount; i++) {
      columnNames.add(Intgen.words(1, 3));
    }

    var data = new Vector<Vector<String>>();

    for (int i = 0; i < rowCount; i++) {
      var row = new Vector<String>();

      for (var j = 0; j < columnCount; j++) {
        row.add(Intgen.words(1, 3));
      }

      data.add(row);
    }

    var model = new DefaultTableModel(data, columnNames);
    var table = new JTable(model);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    for (var i = 0; i < table.getColumnCount(); i++) {
      var column = table.getColumnModel().getColumn(i);
      var width = Intgen.RANDOM.nextInt(50, 120);
      column.setPreferredWidth(width);
    }

    var selected = Intgen.RANDOM.nextInt(-1, rowCount);

    if (selected >= 0) {
      table.setRowSelectionInterval(selected, selected);
    }

    return new JScrollPane(table);
  }
}
