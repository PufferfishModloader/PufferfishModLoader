package me.dreamhopping.pml.mods;

import me.dreamhopping.pml.api.client.Minecraft;
import me.dreamhopping.pml.api.client.gui.FontRenderer;
import me.dreamhopping.pml.events.EventBus;
import me.dreamhopping.pml.events.InvokeEvent;
import me.dreamhopping.pml.events.core.client.chat.ClientChatReceivedEvent;
import me.dreamhopping.pml.events.core.client.chat.ClientChatSentEvent;
import me.dreamhopping.pml.events.core.client.net.ClientDisconnectedEvent;
import me.dreamhopping.pml.events.core.client.net.ClientJoinServerEvent;
import me.dreamhopping.pml.events.core.client.player.ClientItemDropEvent;
import me.dreamhopping.pml.events.core.client.player.ClientPlayerRespawnEvent;
import me.dreamhopping.pml.events.core.client.tick.ClientTickEvent;
import me.dreamhopping.pml.events.core.gui.ClientRenderGameOverlayEvent;
import me.dreamhopping.pml.events.core.mod.ModInitEvent;
import me.dreamhopping.pml.mods.core.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

@Mod("bruh")
public class TestMod {
    public final Logger logger = LogManager.getLogger("TestMod");

    public TestMod() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onInit(ModInitEvent event) {
        logger.info("Hello world! TestMod is now awake...");
    }

    @InvokeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        logger.info("TestMod received a chat message: " + event.message);

        switch (event.message) {
            case "debugfps": {
                logger.info("debug fps: " + Minecraft.getInstance().getFPS());
                break;
            }
            case "sessioninfo": {
                logger.info("session info: " + Minecraft.getInstance().getSessionInfo());
                break;
            }
            case "ispaused": {
                logger.info("paused: " + Minecraft.getInstance().isGamePaused());
                break;
            }
            case "is64bit": {
                logger.info("is 64 bit: " + Minecraft.getInstance().is64Bit());
                break;
            }
        }
    }

    @InvokeEvent
    public void onChatSent(ClientChatSentEvent event) {
        logger.info("Client sent message to server: " + event.message);
    }

    @InvokeEvent
    public void onItemDrop(ClientItemDropEvent event) {
        logger.info("The client has dropped an item!");
    }

    @InvokeEvent
    public void onPlayerRespawn(ClientPlayerRespawnEvent event) {
        logger.info("The player has respawned!");
    }

    @InvokeEvent
    public void onJoinServer(ClientJoinServerEvent event) {
        logger.info("The client has connected to a server! " + (event.isLocal ? "It is a local server! (singleplayer)" : "It is an external server (multiplayer) Address: " + ((InetSocketAddress) event.address).getHostString()));
    }

    @InvokeEvent
    public void onDisconnect(ClientDisconnectedEvent event) {
        logger.info("The client has been disconnected from the server. Reason: " + event.reason);
    }

    @InvokeEvent
    public void onTick(ClientTickEvent event) {
        // WARNING: Do not uncomment this unless you want some sweet sweet log spam
        // logger.info("Tick!");
    }

    @InvokeEvent
    public void onRenderTick(ClientRenderGameOverlayEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        mc.getFontRenderer().drawString("PufferfishModLoader TestMod", 5, 5, -1, true);
        mc.getFontRenderer().drawString("MC Version: " + mc.getVersion().version, 5, 15, -1, true);
        mc.getFontRenderer().drawString("FPS: " + mc.getFPS(), 5, 25, -1, true);
        mc.getFontRenderer().drawString("Paused? " + (mc.isGamePaused() ? "yes" : "no"), 5, 35, -1, true);
    }
}
