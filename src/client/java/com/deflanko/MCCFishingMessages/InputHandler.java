package com.deflanko.MCCFishingMessages;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class InputHandler {
    private static KeyBinding toggleVisibilityKey;
    private static KeyBinding increaseFontSize;
    private static KeyBinding decreaseFontSize;
    private static KeyBinding enterEditMode;
    //private static KeyBinding enterDebugMode;

    
    public static void init() {
        // Register keybinding to toggle chat box visibility
        toggleVisibilityKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "MCC Fish Chatbox Toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F9,
            "MCC Fishing Messages"
        ));

        increaseFontSize = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Font Size - Increase",
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                "MCC Fishing Messages"
        ));

        decreaseFontSize = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Font Size - Decrease",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                "MCC Fishing Messages"
        ));

        enterEditMode = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Enable Edit Mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_ALT,
                "MCC Fishing Messages"
        ));
        /*enterDebugMode = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Enable Debug Mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_9,
                "MCC Fishing Messages"
        ));*/
        
        // Register mouse handlers through Fabric's event system
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleVisibilityKey.wasPressed()) {
                // Toggle chat box visibility
                MCCFishingMessagesMod.fishingChatBox.toggleVisibility();
            }

            while (increaseFontSize.wasPressed()){
                MCCFishingMessagesMod.fishingChatBox.changeFontSize(0.05f);
            }
            while (decreaseFontSize.wasPressed()){
                MCCFishingMessagesMod.fishingChatBox.changeFontSize(-0.05f);
            }
            while (enterEditMode.wasPressed()){
                MCCFishingMessagesMod.fishingChatBox.ToggleEditMode();
            }

            /*while (enterDebugMode.wasPressed()){
                MCCFishingMessagesMod.fishingChatBox.ToggleDebug();
            }*/

        });
    }
}