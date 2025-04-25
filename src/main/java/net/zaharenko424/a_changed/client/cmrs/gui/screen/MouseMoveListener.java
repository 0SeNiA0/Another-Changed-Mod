package net.zaharenko424.a_changed.client.cmrs.gui.screen;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.Widget;

public interface MouseMoveListener extends ContainerEventHandler {

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        //Assume highest first
        boolean consumed = false;
        for(GuiEventListener listener : children()){
            if(!(listener instanceof Widget widget)) continue;
            if(consumed) {//Tell widget to unHover
                if(widget.isHovering()) widget.mouseMoved(Double.NaN, Double.NaN);
            } else {
                widget.mouseMoved(mouseX, mouseY);
                consumed = widget.isHovering();
            }
        }
    }
}
