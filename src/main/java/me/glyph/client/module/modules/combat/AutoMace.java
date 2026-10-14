package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;

import java.util.Random;

public class AutoMace extends Module {
    private final Setting<Double> range = Setting.number("Range", "Range", 3.5, 1.0, 5.0);
    private final Setting<Double> delay = Setting.number("Delay", "Delay (ms)", 120.0, 0.0, 400.0);
    private final Setting<Boolean> onFall = Setting.bool("OnFall", "Only on fall damage", true);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Random", 40.0, 0.0, 120.0);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Humanize", true);
    
    private long lastAttack = 0;
    private final Random random = new Random();
    
    public AutoMace() {
        super("AutoMace", "Automatically uses mace on eligible targets", Category.COMBAT);
        registerSetting(range);
        registerSetting(delay);
        registerSetting(onFall);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(humanize);
    }
    
    @Override
    public void onEnable() {
        lastAttack = System.currentTimeMillis();
    }
    
    @Override
    public void onDisable() {
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null || mc.world == null) return;
        if (onFall.getValue() && mc.player.fallDistance < 3.0f) return;
        long currentTime = System.currentTimeMillis();
        long d = delay.getValue().longValue();
        if (randomDelay.getValue()) {
            d += random.nextInt(randomization.getValue().intValue() + 1);
        }
        if (currentTime - lastAttack >= d) {
            lastAttack = currentTime;
        }
    }
}
