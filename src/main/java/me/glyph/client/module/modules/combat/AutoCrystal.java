package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class AutoCrystal extends Module {
    private final Setting<Double> range = Setting.number("Range", "Attack range", 5.0, 2.0, 6.0);
    private final Setting<Double> delay = Setting.number("Delay", "Attack delay (ms)", 100.0, 0.0, 500.0);
    private final Setting<Boolean> autoPlace = Setting.bool("AutoPlace", "Automatically place crystals", false);
    private final Setting<Double> placeRange = Setting.number("PlaceRange", "Place range", 4.5, 2.0, 6.0);
    private final Setting<Boolean> onlyPlayers = Setting.bool("OnlyPlayers", "Target players", true);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize delays", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Randomization amount", 30.0, 0.0, 100.0);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Human-like behavior", true);
    private final Setting<Double> missChance = Setting.number("MissChance", "Miss chance %", 2.0, 0.0, 20.0);
    
    private long lastAttack = 0;
    private final Random random = new Random();
    
    public AutoCrystal() {
        super("AutoCrystal", "Automatically break crystals", Category.COMBAT);
        registerSetting(range);
        registerSetting(delay);
        registerSetting(autoPlace);
        registerSetting(placeRange);
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
        if (event.isPre() || mc.player == null || mc.world == null || mc.getNetworkHandler() == null) return;
        if (mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL && mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL && autoPlace.getValue()) {
            // Can still break crystals without holding
        }
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
        if (closestCrystal != null) {
            long currentTime = System.currentTimeMillis();
            long attackDelay = delay.getValue().longValue();
            if (randomDelay.getValue()) {
                attackDelay += random.nextInt(randomization.getValue().intValue() + 1);
            }
            if (humanize.getValue() && random.nextDouble() < (missChance.getValue() / 100.0)) {
                return;
            }
            if (currentTime - lastAttack >= attackDelay) {
                mc.interactionManager.attackEntity(mc.player, closestCrystal);
                mc.player.swingHand(Hand.MAIN_HAND);
                lastAttack = currentTime;
            }
        }
    }
}
