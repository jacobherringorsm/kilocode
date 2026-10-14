package me.glyph.client.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static float[] getRotationsToEntity(Entity entity) {
        if (mc.player == null || entity == null) return new float[]{0, 0};
        Vec3d eyesPos = mc.player.getEyePos();
        Vec3d entityPos = entity.getBoundingBox().getCenter();
        double diffX = entityPos.x - eyesPos.x;
        double diffY = entityPos.y - eyesPos.y;
        double diffZ = entityPos.z - eyesPos.z;
        double distance = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F);
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, distance)));
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }
}
