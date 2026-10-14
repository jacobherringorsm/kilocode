package me.glyph.client.module;

import me.glyph.client.GlyphClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    
    private final String name;
    private final String description;
    private final Category category;
    private int key = GLFW.GLFW_KEY_UNKNOWN;
    private boolean enabled;
    private boolean drawn = true;
    protected final List<Setting<?>> settings = new ArrayList<>();
    
    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
    
    public abstract void onEnable();
    public abstract void onDisable();
    
    public void toggle() {
        setEnabled(!enabled);
    }
    
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            onEnable();
            GlyphClient.getInstance().getEventManager().register(this);
        } else {
            GlyphClient.getInstance().getEventManager().unregister(this);
            onDisable();
        }
    }
    
    protected <T> Setting<T> registerSetting(Setting<T> setting) {
        settings.add(setting);
        return setting;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public int getKey() {
        return key;
    }
    
    public void setKey(int key) {
        this.key = key;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public boolean isDrawn() {
        return drawn;
    }
    
    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }
    
    public List<Setting<?>> getSettings() {
        return settings;
    }
}
