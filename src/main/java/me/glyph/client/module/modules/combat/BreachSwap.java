package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;

import java.util.Random;

public class BreachSwap extends Module {
    private final Setting<Double> delay = Setting.number("Delay", "Swap delay (ms)", 80.0, 0.0, 250.0);
    private final Setting<Boolean> swapBack = Setting.bool("SwapBack", "Swap back after", true);
    private final Setting<Double> swapBackDelay = Setting.number("SwapBackDelay", "Swap back delay", 100.0, 10.0, 300.0);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Random", 30.0, 0.0, 80.0);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Humanize", true);
    
    private long lastSwap = 0;
    private boolean swapped = false;
    private final Random random = new Random();
    
    public BreachSwap() {
        super("BreachSwap", "Quick tool swaps for breach scenarios", Category.COMBAT);
        registerSetting(delay);
        registerSetting(swapBack);
        registerSetting(swapBackDelay);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(humanize);
    }
    
    @Override
    public void onEnable() {
        swapped = false;
        lastSwap = System.currentTimeMillis();
    }
    
    @Override
    public void onDisable() {
        swapped = false;
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null) return;
        long currentTime = System.currentTimeMillis();
        long d = delay.getValue().longValue();
        if (randomDelay.getValue()) {
            d += random.nextInt(randomization.getValue().intValue() + 1);
        }
        if (currentTime - lastSwap >= d) {
            if (humanize.getValue() && random.nextDouble() < 0.08) return;
            lastSwap = currentTime;
        }
    }
}
