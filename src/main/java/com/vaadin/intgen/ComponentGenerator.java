package com.vaadin.intgen;

import java.awt.Component;

public interface ComponentGenerator {

    default Component generate() {
        var component = _generate();
        component.setName(getCategory());
        return component;
    }

    Component _generate();

    default String getCategory() {
        return getClass().getSimpleName();
    }
}
