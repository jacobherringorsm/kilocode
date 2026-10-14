package me.glyph.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.glyph.client.GlyphClient;
import me.glyph.client.module.Module;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configDir = FabricLoader.getInstance().getConfigDir().resolve("glyphclient").toFile();
    private final File configFile = new File(configDir, "config.json");
    
    public ConfigManager() {
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }
    
    public void save() {
        Config config = new Config();
        for (Module module : GlyphClient.getInstance().getModuleManager().getModules()) {
            config.moduleStates.put(module.getName(), module.isEnabled());
            config.moduleKeys.put(module.getName(), module.getKey());
            for (var setting : module.getSettings()) {
                config.settings.put(module.getName() + "." + setting.getName(), setting.getValue());
            }
        }
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void load() {
        if (!configFile.exists()) {
            save();
            return;
        }
        try (FileReader reader = new FileReader(configFile)) {
            Config config = gson.fromJson(reader, Config.class);
            if (config == null) return;
            for (Module module : GlyphClient.getInstance().getModuleManager().getModules()) {
                Boolean state = config.moduleStates.get(module.getName());
                if (state != null) {
                    module.setEnabled(state);
                }
                Integer key = config.moduleKeys.get(module.getName());
                if (key != null) {
                    module.setKey(key);
                }
                for (var setting : module.getSettings()) {
                    Object value = config.settings.get(module.getName() + "." + setting.getName());
                    if (value != null) {
                        try {
                            if (setting.isBoolean()) {
                                setting.setValue((Boolean) value);
                            } else if (setting.isNumber()) {
                                if (setting.getValue() instanceof Integer) {
                                    setting.setValue(((Double) value).intValue());
                                } else if (setting.getValue() instanceof Float) {
                                    setting.setValue(((Double) value).floatValue());
                                } else if (setting.getValue() instanceof Double) {
                                    setting.setValue((Double) value);
                                }
                            } else if (setting.isMode()) {
                                setting.setValue((String) value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
