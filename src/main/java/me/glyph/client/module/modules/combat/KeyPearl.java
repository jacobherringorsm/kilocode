package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.event.events.KeyEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import me.glyph.client.util.InventoryUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class KeyPearl extends Module {
    private final Setting<Integer> key = Setting.number("Key", "Keybind", GLFW.GLFW_KEY_V, 0, 512);
    private final Setting<Double> delay = Setting.number("Delay", "Throw delay (ms)", 50.0, 0.0, 150.0);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Random", 15.0, 0.0, 50.0);
    private final Setting<Boolean> preferHotbar = Setting.bool("PreferHotbar", "Prefer hotbar pearls", true);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Humanize", true);
    
    private long lastThrow = 0;
    private final Random random = new Random();
    private boolean thrown = false;
    
    public KeyPearl() {
        super("KeyPearl", "Throw pearl on keybind", Category.COMBAT);
        registerSetting(key);
        registerSetting(delay);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(preferHotbar);
        registerSetting(humanize);
    }
    
    @Override
    public void onEnable() {
        lastThrow = System.currentTimeMillis();
        thrown = false;
    }
    
    @Override
    public void onDisable() {
        thrown = false;
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null || mc.world == null) return;
    }
    
    @SubscribeEvent
    public void onKey(KeyEvent event) {
        if (event.getAction() != 1) return;
        if (event.getKey() == key.getValue()) {
            throwPearl();
        }
    }
    
    private void throwPearl() {
        long currentTime = System.currentTimeMillis();
        long d = delay.getValue().longValue();
        if (randomDelay.getValue()) {
            d += random.nextInt(randomization.getValue().intValue() + 1);
        }
        if (currentTime - lastThrow < d) return;
        int slot = InventoryUtils.findItemSlot(Items.ENDER_PEARL);
        if (slot == -1) return;
        int prev = mc.player.getInventory().getSelectedSlot();
        if (slot < 9) {
            mc.player.getInventory().setSelectedSlot(slot);
            if (mc.interactionManager != null) {
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            }
            mc.player.getInventory().setSelectedSlot(prev);
            lastThrow = currentTime;
        }
    }
}
