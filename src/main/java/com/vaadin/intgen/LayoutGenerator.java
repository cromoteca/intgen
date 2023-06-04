package com.vaadin.intgen;

import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public abstract class LayoutGenerator implements ComponentGenerator<JComponent> {

    @Override
    public JComponent add(Container parent) {
        var container = generate();
        var wrapper = generateWrapper(container);

        if (Intgen.RANDOM.nextDouble() > 0.8) {
            wrapper.setBorder(BorderFactory.createTitledBorder(Intgen.words(1, 3)));
        } else {
            var padding = Intgen.RANDOM.nextInt(10);
            wrapper.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        }

        container.setName(getCategory());
        parent.add(wrapper, Intgen.RANDOM.nextInt(-1, parent.getComponentCount()));
        return container;
    }

    public JComponent generateWrapper(JComponent container) {
        return container;
    }
}
