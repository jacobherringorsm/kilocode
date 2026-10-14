package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;

import java.util.Random;

public class AutoPearlCatch extends Module {
    private final Setting<Double> range = Setting.number("Range", "Detection range", 10.0, 3.0, 20.0);
    private final Setting<Double> speed = Setting.number("Speed", "Catch speed", 1.0, 0.1, 5.0);
    private final Setting<Boolean> predict = Setting.bool("Predict", "Predict pearl trajectory", true);
    private final Setting<Boolean> randomize = Setting.bool("Randomize", "Randomize movement", true);
    private final Setting<Double> randomAmount = Setting.number("RandomAmount", "Random amount", 0.3, 0.0, 1.0);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Human-like reaction", true);
    private final Setting<Double> reactionTime = Setting.number("ReactionTime", "Reaction time (ms)", 50.0, 0.0, 200.0);
    
    private long lastCatch = 0;
    private final Random random = new Random();
    
    public AutoPearlCatch() {
        super("AutoPearlCatch", "Attempts to catch pearls automatically", Category.COMBAT);
        registerSetting(range);
        registerSetting(speed);
        registerSetting(predict);
        registerSetting(randomize);
        registerSetting(randomAmount);
        registerSetting(humanize);
        registerSetting(reactionTime);
    }
    
    @Override
    public void onEnable() {
        lastCatch = System.currentTimeMillis();
    }
    
    @Override
    public void onDisable() {
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null || mc.world == null) return;
        long currentTime = System.currentTimeMillis();
        if (humanize.getValue() && currentTime - lastCatch < reactionTime.getValue().longValue()) {
            return;
        }
        lastCatch = currentTime;
    }
}
