package com.example.mccfishingchat;

import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.main.Main;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.*;

public class FishingChatBox {
    private static final int MAX_MESSAGES = 100;
    private static final int MAX_VISIBLE_MESSAGES = 10;
    //private static final int MESSAGE_FADE_TIME = 200;
    //private static final int MESSAGE_STAY_TIME = 10000; // 10 seconds
    private static final int BACKGROUND_COLOR = 0x80000000; // Semi-transparent black
    
    private final MinecraftClient client;
    private final Deque<ChatMessage> messages = new LinkedList<>();
    private int scrollOffset = 0;
    private boolean focused = false;
    private boolean visible = true;


    private final int boxX = 0;  // Default position
    private final int boxY = 30; // Top of screen, below hot bar
    private final int boxWidth = 330;
    private final int boxHeight = 110;

    private int guiScaleFactor = 1;
    
    public FishingChatBox(MinecraftClient client) {
        this.client = client;
        //updateGuiScale();
    }
    
    public void render(DrawContext context, int mouseX, int mouseY, RenderTickCounter tickCounter) {
        if (!visible || messages.isEmpty()) return;
        
        // Draw background
        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, BACKGROUND_COLOR);
        if(focused){
            context.drawBorder(boxX, boxY, boxWidth+2, boxHeight+2, 0xFFFFFFFF);
        }
        
        // Draw title
        String title = "Fishing Messages";
        //String subtitle = "(click here to scroll)";
        String cords = "X: " + (int)client.player.getX() + " Y: " + (int)client.player.getY() + " Z: " + (int)client.player.getZ();
        context.drawText(client.textRenderer, title, boxX + 5, boxY + 5, 0xFFFFFFFF, true);
        //context.drawText(client.textRenderer, subtitle, boxX + 100, boxY + 5, 0xFF00DCFF, true);
        context.drawText(client.textRenderer, cords, boxX + 200, boxY + 5, 0xFFA000, true);

        
        // Draw messages
        int chatMessageHeight =  (boxHeight - 25)/MAX_VISIBLE_MESSAGES;
        int yOffset = boxY + boxHeight - chatMessageHeight; // Start at bottom of box
        int visibleCount = 0;

        List<ChatMessage> visibleMessages = new ArrayList<>(messages);
        int startIndex = Math.max(0, Math.min(scrollOffset, messages.size() - MAX_VISIBLE_MESSAGES));

        for (int i = startIndex; i < visibleMessages.size() && visibleCount < MAX_VISIBLE_MESSAGES; i++) {
            ChatMessage message = visibleMessages.get(i);
            List<OrderedText> wrappedText = new ArrayList<>(client.textRenderer.wrapLines(message.text, boxWidth - 5));
            reverseList(wrappedText);
            for (OrderedText line : wrappedText) {
                if (visibleCount >=MAX_VISIBLE_MESSAGES){
                    continue;
                }
                context.drawText(client.textRenderer, line, boxX + 5, yOffset, 0xFFFFFFFF, true);
                yOffset -= chatMessageHeight + 1;
                visibleCount++;
            }

            //visibleCount++;
        }

        // Draw scroll bar if needed
        if (messages.size() > MAX_VISIBLE_MESSAGES) {
            int scrollBarHeight = boxHeight - 25;
            int thumbSize = Math.max(10, scrollBarHeight * MAX_VISIBLE_MESSAGES / messages.size());
            int thumbPosition = scrollOffset * (scrollBarHeight - thumbSize) / (messages.size() - MAX_VISIBLE_MESSAGES);

            // Scroll bar background
            context.fill(boxX + boxWidth - 5, boxY + 20, boxX + boxWidth - 2, boxY + boxHeight - 5, 0x40FFFFFF);
            // Scroll thumb
            context.fill(boxX + boxWidth - 5, boxY + boxHeight - 5 - thumbPosition, boxX + boxWidth - 2,
                    boxY + boxHeight - 5 - thumbPosition - thumbSize, 0xFFAAAAAA);
        }
    }
    public static <T> void reverseList(List<T> list) {
        // base condition when the list size is 0
        if (list.size() <= 1 )
            return;

        T value = list.removeFirst();

        // call the recursive function to reverse
        // the list after removing the first element
        reverseList(list);

        // now after the rest of the list has been
        // reversed by the upper recursive call,
        // add the first value at the end
        list.add(value);
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
    
    public void mouseClicked(double mouseX, double mouseY, int button) {
        updateGuiScale();
        focused = visible && mouseX >= boxX && mouseX <= (boxX + boxWidth)*guiScaleFactor &&
                 mouseY >= boxY*guiScaleFactor && mouseY <= (boxY + boxHeight)*guiScaleFactor && button == 0 && client.inGameHud.getChatHud().isChatFocused();
    }

    //unused mouse drag section
    /*public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (focused && button == 0 && mouseX >= boxX && mouseX <= boxX + boxWidth && 
            mouseY >= boxY && mouseY <= boxY + boxHeight) {
            boxX += deltaX;
            boxY += deltaY;
        }
    }*/
    
    public boolean isFocused() {
        return focused;
    }
    
    public void toggleVisibility() {
        visible = !visible;
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void updateGuiScale(){
        this.guiScaleFactor = client.options.getGuiScale().getValue();

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