package com.vaadin.intgen;

import java.awt.Component;

public interface ComponentGenerator {

    Component generate();

    default String getCategory() {
        return getClass().getSimpleName();
    }
}
