package me.dreamhopping.pml.transformers.impl;

import me.dreamhopping.pml.events.EventBus;
import me.dreamhopping.pml.events.core.gui.ClientRenderGameOverlayEvent;
import me.dreamhopping.pml.transformers.GuiIngameTransformer;

/**
 * An implementation for the GuiIngameTransformer class
 *
 * @see GuiIngameTransformer
 */
public class GuiIngameTransformerImpl {
    public static void renderGameOverlay() {
        EventBus.INSTANCE.post(new ClientRenderGameOverlayEvent());
    }
}
