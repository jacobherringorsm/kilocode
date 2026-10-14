package me.glyph.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static List<Entity> getEntitiesInRange(double range) {
        if (mc.player == null || mc.world == null) return new ArrayList<>();
        Vec3d pos = mc.player.getPos();
        return mc.world.getEntities().stream()
                .filter(e -> e != mc.player)
                .filter(e -> e.squaredDistanceTo(pos) <= range * range)
                .collect(Collectors.toList());
    }
    
    public static List<PlayerEntity> getPlayersInRange(double range) {
        if (mc.player == null || mc.world == null) return new ArrayList<>();
        Vec3d pos = mc.player.getPos();
        return mc.world.getPlayers().stream()
                .filter(e -> e != mc.player)
                .filter(e -> e.isAlive())
                .filter(e -> e.squaredDistanceTo(pos) <= range * range)
                .collect(Collectors.toList());
    }
    
    public static List<LivingEntity> getLivingEntitiesInRange(double range) {
        if (mc.player == null || mc.world == null) return new ArrayList<>();
        Vec3d pos = mc.player.getPos();
        return mc.world.getEntitiesByClass(LivingEntity.class, mc.player.getBoundingBox().expand(range), e -> true)
                .stream()
                .filter(e -> e != mc.player)
                .filter(e -> e.isAlive())
                .sorted(Comparator.comparingDouble(e -> mc.player.squaredDistanceTo(e)))
                .collect(Collectors.toList());
    }
}
