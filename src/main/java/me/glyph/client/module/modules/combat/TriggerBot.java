package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Random;

public class TriggerBot extends Module {
    private final Setting<Double> range = Setting.number("Range", "Attack range", 4.0, 1.0, 6.0);
    private final Setting<Double> delay = Setting.number("Delay", "Delay between attacks (ms)", 150.0, 0.0, 1000.0);
    private final Setting<Boolean> onlyPlayers = Setting.bool("OnlyPlayers", "Only attack players", true);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Add randomization to delay", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Max random delay", 50.0, 0.0, 200.0);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Human-like behavior", true);
    private final Setting<Double> missChance = Setting.number("MissChance", "Chance to miss", 5.0, 0.0, 30.0);
    
    private long lastAttack = 0;
    private final Random random = new Random();
    
    public TriggerBot() {
        super("TriggerBot", "Automatically attacks when looking at entity", Category.COMBAT);
        registerSetting(range);
        registerSetting(delay);
        registerSetting(onlyPlayers);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(humanize);
        registerSetting(missChance);
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
        if (event.isPre() || mc.player == null || mc.world == null || mc.crosshairTarget == null) return;
        if (!(mc.crosshairTarget instanceof net.minecraft.util.hit.EntityHitResult hit)) return;
        if (!(hit.getEntity() instanceof LivingEntity target)) return;
        if (target == mc.player) return;
        if (onlyPlayers.getValue() && !(target instanceof PlayerEntity)) return;
        if (mc.player.distanceTo(target) > range.getValue()) return;
        if (!target.isAlive()) return;
        
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
                mc.interactionManager.attackEntity(mc.player, target);
            }
            mc.player.swingHand(Hand.MAIN_HAND);
            lastAttack = currentTime;
        }
    }
}
