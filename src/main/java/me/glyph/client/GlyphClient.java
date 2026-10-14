package me.glyph.client;

import me.glyph.client.event.EventManager;
import me.glyph.client.config.ConfigManager;
import me.glyph.client.module.ModuleManager;
import me.glyph.client.gui.ClickGui;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlyphClient implements ClientModInitializer {
    public static final String MOD_ID = "glyphclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final MinecraftClient MC = MinecraftClient.getInstance();
    
    private static GlyphClient INSTANCE;
    
    private EventManager eventManager;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private ClickGui clickGui;
    
    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER.info("Initializing Glyph Client");
        
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();
        clickGui = new ClickGui();
        
        configManager.load();
        LOGGER.info("Glyph Client initialized!");
    }
    
    public static GlyphClient getInstance() {
        return INSTANCE;
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public ClickGui getClickGui() {
        return clickGui;
    }
}
