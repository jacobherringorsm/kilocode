package me.glyph.client.module.modules.combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Random;

public class AnchorMacro extends Module {
    private final Setting<Double> delay = Setting.number("Delay", "Click delay (ms)", 150.0, 0.0, 500.0);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize delay", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Random amount", 40.0, 0.0, 150.0);
    private final Setting<Boolean> requireGlowstone = Setting.bool("RequireGlowstone", "Require glowstone/charges", true);
    private final Setting<Integer> charges = Setting.number("Charges", "Min charges", 1, 1, 4);
    
    private long lastClick = 0;
    private final Random random = new Random();
    
    public AnchorMacro() {
        super("AnchorMacro", "Macro for respawn anchors", Category.COMBAT);
        registerSetting(delay);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(requireGlowstone);
        registerSetting(charges);
    }
    
    @Override
    public void onEnable() {
        lastClick = System.currentTimeMillis();
    }
    
    @Override
    public void onDisable() {
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null || mc.world == null) return;
        if (mc.player.getMainHandStack().getItem() != Items.RESPAWN_ANCHOR) return;
        long currentTime = System.currentTimeMillis();
        long d = delay.getValue().longValue();
        if (randomDelay.getValue()) {
            d += random.nextInt(randomization.getValue().intValue() + 1);
        }
        if (currentTime - lastClick >= d) {
            // Right click logic handled by interaction manager in context; simplified
            lastClick = currentTime;
        }
    }
}
