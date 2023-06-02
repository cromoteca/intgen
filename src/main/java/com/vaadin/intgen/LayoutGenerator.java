package com.vaadin.intgen;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

public abstract class LayoutGenerator implements ComponentGenerator {

    private final List<ComponentGenerator> children = new ArrayList<>();

    @Override
    public Container generate() {
        var container = _generate();
        container.setName(getCategory());

        for (var child : children) {
            container.add(child.generate());
        }

        return container;
    }

    public boolean forbid(String parentCategory) {
        return false;
    }

    public void addChild(ComponentGenerator child) {
        children.add(child);
    }

    public List<ComponentGenerator> getChildren() {
        return children;
    }

    @Override
    public abstract Container _generate();
}
