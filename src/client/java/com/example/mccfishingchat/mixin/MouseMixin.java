package com.example.mccfishingchat.mixin;

import com.example.mccfishingchat.MCCFishingChatMod;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseScroll", at = @At("RETURN"))
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (MCCFishingChatMod.isOnMCCIsland() && MCCFishingChatMod.fishingChatBox != null && MCCFishingChatMod.fishingChatBox.isFocused()) {
            MCCFishingChatMod.fishingChatBox.scroll((int) vertical);
        }
    }

    
    @Inject(method = "onMouseButton", at = @At("RETURN"))
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (MCCFishingChatMod.isOnMCCIsland() && MCCFishingChatMod.fishingChatBox != null && action == 1) { // 1 = press
            double x = MCCFishingChatMod.CLIENT.mouse.getX();
            double y = MCCFishingChatMod.CLIENT.mouse.getY();
            MCCFishingChatMod.fishingChatBox.mouseClicked(x, y);
        }
    }
}
