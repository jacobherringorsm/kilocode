package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;

import java.util.Random;

public class AutoSafeAnchorMacro extends Module {
    private final Setting<Double> healthThreshold = Setting.number("HealthThreshold", "Health to trigger", 10.0, 2.0, 20.0);
    private final Setting<Double> delay = Setting.number("Delay", "Delay", 100.0, 0.0, 300.0);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Random", 30.0, 0.0, 100.0);
    private final Setting<Boolean> predictMovement = Setting.bool("PredictMovement", "Predict safe spot", true);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Humanize timings", true);
    
    private long lastAction = 0;
    private final Random random = new Random();
    
    public AutoSafeAnchorMacro() {
        super("AutoSafeAnchorMacro", "Safely places/uses anchor when low health", Category.COMBAT);
        registerSetting(healthThreshold);
        registerSetting(delay);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(predictMovement);
        registerSetting(humanize);
    }
    
    @Override
    public void onEnable() {
        lastAction = System.currentTimeMillis();
    }
    
    @Override
    public void onDisable() {
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null || mc.world == null) return;
        if (mc.player.getHealth() > healthThreshold.getValue()) return;
        long currentTime = System.currentTimeMillis();
        long d = delay.getValue().longValue();
        if (randomDelay.getValue()) {
            d += random.nextInt(randomization.getValue().intValue() + 1);
        }
        if (currentTime - lastAction >= d) {
            lastAction = currentTime;
        }
    }
}
