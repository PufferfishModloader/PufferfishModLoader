package me.dreamhopping.pml.transformers.impl;

import me.dreamhopping.pml.events.EventBus;
import me.dreamhopping.pml.events.core.client.net.ClientDisconnectedEvent;
import me.dreamhopping.pml.events.core.client.net.ClientJoinServerEvent;
import net.minecraft.util.IChatComponent;

import java.net.SocketAddress;

/**
 * An implementation for the NetHandlerPlayClientTransformer class
 * @see me.dreamhopping.pml.transformers.NetHandlerPlayClientTransformer
 */
public class NetHandlerPlayClientTransformerImpl {
    public static void handleJoinGame(boolean isLocal, SocketAddress address) {
        EventBus.INSTANCE.post(new ClientJoinServerEvent(isLocal, address));
    }

    public static void handleDisconnect(IChatComponent reason) {
        EventBus.INSTANCE.post(new ClientDisconnectedEvent(reason.getUnformattedText()));
    }
}
