package me.glyph.client.event.events;

import me.glyph.client.event.Event;
import net.minecraft.entity.Entity;

public class AttackEntityEvent extends Event {
    private final Entity entity;
    
    public AttackEntityEvent(Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }
}
