package me.glyph.client.event.events;

import me.glyph.client.event.Event;

public class WorldRenderEvent extends Event {
    private final float tickDelta;
    
    public WorldRenderEvent(float tickDelta) {
        this.tickDelta = tickDelta;
    }
    
    public float getTickDelta() {
        return tickDelta;
    }
}
