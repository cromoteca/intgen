package com.vaadin.intgen;

public class Generator {
  public void createDataSet() {
    createDataSet(DataSet.Split.values());
  }

  public void createDataSet(DataSet.Split... splits) {
    for (var split : splits) {
      new DataSet(split).create();
    }
  }
}
