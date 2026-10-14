package me.glyph.client.event.events;

import me.glyph.client.event.Event;

public class UpdateEvent extends Event {
    private final boolean pre;
    
    public UpdateEvent(boolean pre) {
        this.pre = pre;
    }
    
    public boolean isPre() {
        return pre;
    }
}
