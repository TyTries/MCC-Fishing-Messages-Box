package com.example.mccfishingchat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class FishingChatBox {
    private static final int MAX_MESSAGES = 100;
    private static final int MAX_VISIBLE_MESSAGES = 10;
    private static final int MESSAGE_FADE_TIME = 200;
    private static final int MESSAGE_STAY_TIME = 10000; // 10 seconds
    private static final int BACKGROUND_COLOR = 0x80000000; // Semi-transparent black
    
    private final MinecraftClient client;
    private final Deque<ChatMessage> messages = new LinkedList<>();
    private int scrollOffset = 0;
    private boolean focused = false;
    private boolean visible = true;
    
    private int boxX = 5;  // Default position
    private int boxY = 30; // Top of screen, below hotbar
    private int boxWidth = 320;
    private int boxHeight = 120;
    
    public FishingChatBox(MinecraftClient client) {
        this.client = client;
    }
    
    public void render(DrawContext context, int mouseX, int mouseY, RenderTickCounter tickCounter) {
        if (!visible || messages.isEmpty()) return;
        
        // Draw background
        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, BACKGROUND_COLOR);
        
        // Draw title
        String title = "Fishing Messages";
        context.drawText(client.textRenderer, title, boxX + 5, boxY + 5, 0xFFFFFF, true);
        
        // Draw messages
        int chatmsgHeight =  (boxHeight - 25)/MAX_VISIBLE_MESSAGES;
        int yOffset = boxY + boxHeight - chatmsgHeight; // Start at bottom of box
        int visibleCount = 0;

        List<ChatMessage> visibleMessages = new ArrayList<>(messages);
        int startIndex = Math.max(0, Math.min(scrollOffset, messages.size() - MAX_VISIBLE_MESSAGES));

        for (int i = startIndex; i < visibleMessages.size() && visibleCount < MAX_VISIBLE_MESSAGES; i++) {
            ChatMessage message = visibleMessages.get(i);
            List<OrderedText> wrappedText = client.textRenderer.wrapLines(message.text, boxWidth - 10);

            for (OrderedText line : wrappedText) {
                context.drawText(client.textRenderer, line, boxX + 5, yOffset, 0xFFFFFF, true);
                yOffset -= chatmsgHeight;
            }

            visibleCount++;
        }

        // Draw scroll bar if needed
        if (messages.size() > MAX_VISIBLE_MESSAGES) {
            int scrollBarHeight = boxHeight - 25;
            int thumbSize = Math.max(10, scrollBarHeight * MAX_VISIBLE_MESSAGES / messages.size());
            int thumbPosition = scrollOffset * (scrollBarHeight - thumbSize) / (messages.size() - MAX_VISIBLE_MESSAGES);

            // Scroll bar background
            context.fill(boxX + boxWidth - 5, boxY + 20, boxX + boxWidth - 2, boxY + boxHeight - 5, 0x40FFFFFF);
            // Scroll thumb
            context.fill(boxX + boxWidth - 5, boxY + 20 + thumbPosition, boxX + boxWidth - 2,
                     boxY + 20 + thumbPosition + thumbSize, 0xFFAAAAAA);
        }
    }
    
    public void addMessage(Text message) {
        messages.addFirst(new ChatMessage(message, client.inGameHud.getTicks()));
        while (messages.size() > MAX_MESSAGES) {
            messages.removeLast();
        }
    }
    
    public void scroll(int amount) {
        if (focused) {
            scrollOffset = MathHelper.clamp(scrollOffset + amount, 0, Math.max(0, messages.size() - MAX_VISIBLE_MESSAGES));
        }
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        focused = visible && mouseX >= boxX && mouseX <= boxX + boxWidth && 
                 mouseY >= boxY && mouseY <= boxY + boxHeight;
        return focused;
    }
    
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (focused && button == 0 && mouseX >= boxX && mouseX <= boxX + boxWidth && 
            mouseY >= boxY && mouseY <= boxY + boxHeight) {
            boxX += deltaX;
            boxY += deltaY;
        }
    }
    
    public boolean isFocused() {
        return focused;
    }
    
    public void toggleVisibility() {
        visible = !visible;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    private static class ChatMessage {
        public final Text text;
        public final int timestamp;
        
        public ChatMessage(Text text, int timestamp) {
            this.text = text;
            this.timestamp = timestamp;
        }
    }
}