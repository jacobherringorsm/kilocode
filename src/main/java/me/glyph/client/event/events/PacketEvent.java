package me.glyph.client.event.events;

import me.glyph.client.event.Event;
import net.minecraft.network.packet.Packet;

public class PacketEvent extends Event {
    private final Packet<?> packet;
    private final boolean send;
    private boolean cancelled;
    
    public PacketEvent(Packet<?> packet, boolean send) {
        this.packet = packet;
        this.send = send;
        this.cancelled = false;
    }
    
    public Packet<?> getPacket() {
        return packet;
    }
    
    public boolean isSend() {
        return send;
    }
    
    public boolean isReceive() {
        return !send;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
