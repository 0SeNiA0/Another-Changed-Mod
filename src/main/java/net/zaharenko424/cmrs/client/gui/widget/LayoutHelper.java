package net.zaharenko424.cmrs.client.gui.widget;

import java.util.List;

public class LayoutHelper {

    public static <W extends Widget & SizedWidget> void listLayout(ScrollableContainer container, List<W> widgets, float spacing){
        listLayout(container, widgets, 0, spacing);
    }

    public static <W extends Widget & SizedWidget> void listLayout(ScrollableContainer container, List<W> widgets, float topOffset, float spacing){
        if(widgets.isEmpty()){
            container.setActualHeight(0);
            return;
        }

        float top = -container.getHeight() / 2f;
        float actualHeight = topOffset;

        for(W w : widgets){
            w.setOrigin(0,  top + actualHeight + w.getHeight() / 2f, 0);
            container.addWidget(w);

            actualHeight += w.getHeight() + spacing;
        }

        actualHeight -= spacing;
        container.setActualHeight(actualHeight);
    }
}
