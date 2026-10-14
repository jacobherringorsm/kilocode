package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;

import java.util.Random;

public class AutoHitCrystal extends Module {
    private final Setting<Double> range = Setting.number("Range", "Attack range", 4.5, 2.0, 6.0);
    private final Setting<Double> delay = Setting.number("Delay", "Delay between hits (ms)", 90.0, 0.0, 400.0);
    private final Setting<Boolean> onlyInCombat = Setting.bool("OnlyInCombat", "Only when in combat", false);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize delays", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Random amount", 25.0, 0.0, 80.0);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Human-like timing", true);
    private final Setting<Double> missChance = Setting.number("MissChance", "Miss chance %", 3.0, 0.0, 25.0);
    private final Setting<Boolean> rotate = Setting.bool("Rotate", "Silent rotate to crystal", true);
    
    private long lastAttack = 0;
    private final Random random = new Random();
    
    public AutoHitCrystal() {
        super("AutoHitCrystal", "Automatically hits nearby crystals", Category.COMBAT);
        registerSetting(range);
        registerSetting(delay);
        registerSetting(onlyInCombat);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(humanize);
        registerSetting(missChance);
        registerSetting(rotate);
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
        if (event.isPre() || mc.player == null || mc.world == null || mc.getNetworkHandler() == null) return;
        EndCrystalEntity closestCrystal = null;
        double closestDist = Double.MAX_VALUE;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity crystal) {
                double dist = mc.player.distanceTo(crystal);
                if (dist <= range.getValue() && dist < closestDist) {
                    closestDist = dist;
                    closestCrystal = crystal;
                }
            }
        }
        if (closestCrystal == null) return;
        long currentTime = System.currentTimeMillis();
        long attackDelay = delay.getValue().longValue();
        if (randomDelay.getValue()) {
            attackDelay += random.nextInt(randomization.getValue().intValue() + 1);
        }
        if (humanize.getValue() && random.nextDouble() < (missChance.getValue() / 100.0)) {
            return;
        }
        if (currentTime - lastAttack >= attackDelay) {
            if (mc.interactionManager != null) {
                mc.interactionManager.attackEntity(mc.player, closestCrystal);
            }
            mc.player.swingHand(Hand.MAIN_HAND);
            lastAttack = currentTime;
        }
    }
}
