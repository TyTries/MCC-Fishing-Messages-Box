package com.example.mccfishingchat;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class InputHandler {
    private static KeyBinding toggleVisibilityKey;
    
    public static void init() {
        // Register keybinding to toggle chat box visibility
        toggleVisibilityKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.mccfishingchat.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F9,
            "key.categories.mccfishingchat"
        ));
        
        // Register mouse handlers through Fabric's event system
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleVisibilityKey.wasPressed()) {
                // Toggle chat box visibility
                MCCFishingChatMod.fishingChatBox.toggleVisibility();
            }
        });
    }
}