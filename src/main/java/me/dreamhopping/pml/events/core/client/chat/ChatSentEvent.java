package me.dreamhopping.pml.events.core.client.chat;

import me.dreamhopping.pml.events.Event;

/**
 * Called when a chat message is sent by the client
 * @author dreamhopping
 * @see me.dreamhopping.pml.events.Event
 */
public class ChatSentEvent extends Event {
    public String message;

    public ChatSentEvent(String message) {
        this.message = message;
    }
}
