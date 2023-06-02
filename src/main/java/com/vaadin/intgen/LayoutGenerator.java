package com.vaadin.intgen;

import java.awt.Container;

public interface LayoutGenerator extends ComponentGenerator {

    @Override
    Container generate();

}
