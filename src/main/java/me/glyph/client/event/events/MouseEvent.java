package me.glyph.client.event.events;

import me.glyph.client.event.Event;

public class MouseEvent extends Event {
    private final long window;
    private final double mouseX;
    private final double mouseY;
    private final int button;
    private final int action;
    private final int mods;
    
    public MouseEvent(long window, double mouseX, double mouseY, int button, int action, int mods) {
        this.window = window;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
        this.action = action;
        this.mods = mods;
    }
    
    public long getWindow() {
        return window;
    }
    
    public double getMouseX() {
        return mouseX;
    }
    
    public double getMouseY() {
        return mouseY;
    }
    
    public int getButton() {
        return button;
    }
    
    public int getAction() {
        return action;
    }
    
    public int getMods() {
        return mods;
    }
}
