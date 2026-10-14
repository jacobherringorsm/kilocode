package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import me.glyph.client.util.RotationUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AimAssist extends Module {
    private final Setting<Double> range = Setting.number("Range", "Aim range", 6.0, 3.0, 10.0);
    private final Setting<Double> speed = Setting.number("Speed", "Aim speed", 2.0, 0.1, 10.0);
    private final Setting<Double> smoothing = Setting.number("Smoothing", "Smoothing factor", 0.8, 0.1, 1.0);
    private final Setting<Boolean> onlyPlayers = Setting.bool("OnlyPlayers", "Only aim at players", true);
    private final Setting<Boolean> aimWhileAttack = Setting.bool("AimWhileAttacking", "Only aim while attacking", false);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Human-like movement", true);
    private final Setting<Double> jitter = Setting.number("Jitter", "Jitter amount", 0.5, 0.0, 2.0);
    private final Setting<Double> fov = Setting.number("FOV", "Max FOV", 90.0, 15.0, 180.0);
    
    private final Random random = new Random();
    
    public AimAssist() {
        super("AimAssist", "Helps with aiming at targets", Category.COMBAT);
        registerSetting(range);
        registerSetting(speed);
        registerSetting(smoothing);
        registerSetting(onlyPlayers);
        registerSetting(aimWhileAttack);
        registerSetting(humanize);
        registerSetting(jitter);
        registerSetting(fov);
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null || mc.world == null) return;
        if (aimWhileAttack.getValue() && !mc.options.attackKey.isPressed()) return;
        if (mc.crosshairTarget != null && mc.crosshairTarget instanceof net.minecraft.util.hit.EntityHitResult) return;
        
        LivingEntity target = null;
        double closest = Double.MAX_VALUE;
        for (LivingEntity entity : mc.world.getEntitiesByClass(LivingEntity.class, mc.player.getBoundingBox().expand(range.getValue()), e -> true)) {
            if (entity == mc.player || !entity.isAlive()) continue;
            if (onlyPlayers.getValue() && !(entity instanceof PlayerEntity)) continue;
            float[] rot = RotationUtils.getRotationsToEntity(entity);
            float yawDiff = MathHelper.abs(MathHelper.wrapDegrees(rot[0] - mc.player.getYaw()));
            float pitchDiff = MathHelper.abs(MathHelper.wrapDegrees(rot[1] - mc.player.getPitch()));
            double fovDist = Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);
            if (fovDist > fov.getValue()) continue;
            double dist = mc.player.distanceTo(entity);
            if (dist < closest) {
                closest = dist;
                target = entity;
            }
        }
        if (target == null) return;
        float[] targetRot = RotationUtils.getRotationsToEntity(target);
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();
        float diffYaw = MathHelper.wrapDegrees(targetRot[0] - yaw);
        float diffPitch = MathHelper.wrapDegrees(targetRot[1] - pitch);
        float step = speed.getValue().floatValue() * 0.1f;
        if (humanize.getValue()) {
            step *= (0.8 + random.nextDouble() * 0.4);
        }
        diffYaw *= step * smoothing.getValue().floatValue();
        diffPitch *= step * smoothing.getValue().floatValue();
        if (humanize.getValue() && jitter.getValue() > 0) {
            diffYaw += (random.nextDouble() - 0.5) * jitter.getValue() * 0.1f;
            diffPitch += (random.nextDouble() - 0.5) * jitter.getValue() * 0.1f;
        }
        mc.player.setYaw(yaw + diffYaw);
        mc.player.setPitch(pitch + diffPitch);
    }
}
