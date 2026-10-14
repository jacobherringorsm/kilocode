package me.glyph.client.module;

import me.glyph.client.module.modules.combat.*;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    
    public ModuleManager() {
        // Combat modules as requested
        addModule(new TriggerBot());
        addModule(new AimAssist());
        addModule(new AutoCrystal());
        addModule(new AnchorMacro());
        addModule(new AutoSafeAnchorMacro());
        addModule(new AutoInvTotem());
        addModule(new AutoMace());
        addModule(new BreachSwap());
        addModule(new KeyPearl());
        addModule(new AutoPearlCatch());
        addModule(new AutoHitCrystal());
    }
    
    private void addModule(Module module) {
        modules.add(module);
    }
    
    public List<Module> getModules() {
        return modules;
    }
    
    public List<Module> getModulesByCategory(Category category) {
        return modules.stream().filter(m -> m.getCategory() == category).collect(Collectors.toList());
    }
    
    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
    
    public Module getModuleByClass(Class<? extends Module> clazz) {
        for (Module module : modules) {
            if (module.getClass() == clazz) {
                return module;
            }
        }
        return null;
    }
}
