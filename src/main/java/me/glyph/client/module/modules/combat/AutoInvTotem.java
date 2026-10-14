package me.glyph.client.module/modules/combat;

import me.glyph.client.event.SubscribeEvent;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import me.glyph.client.util.InventoryUtils;
import net.minecraft.item.Items;

import java.util.Random;

public class AutoInvTotem extends Module {
    private final Setting<Double> delay = Setting.number("Delay", "Swap delay (ms)", 50.0, 0.0, 200.0);
    private final Setting<Boolean> onlyOffhand = Setting.bool("OnlyOffhand", "Only put in offhand", true);
    private final Setting<Boolean> randomDelay = Setting.bool("RandomDelay", "Randomize", true);
    private final Setting<Double> randomization = Setting.number("Randomization", "Random", 20.0, 0.0, 100.0);
    private final Setting<Boolean> humanize = Setting.bool("Humanize", "Humanize", true);
    private final Setting<Boolean> keepInHotbar = Setting.bool("KeepInHotbar", "Ensure totem in hotbar", true);
    
    private long lastSwap = 0;
    private final Random random = new Random();
    
    public AutoInvTotem() {
        super("AutoInvTotem", "Automatically equips totems to offhand", Category.COMBAT);
        registerSetting(delay);
        registerSetting(onlyOffhand);
        registerSetting(randomDelay);
        registerSetting(randomization);
        registerSetting(humanize);
        registerSetting(keepInHotbar);
    }
    
    @Override
    public void onEnable() {
        lastSwap = System.currentTimeMillis();
    }
    
    @Override
    public void onDisable() {
    }
    
    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || mc.player == null) return;
        if (mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;
        int slot = InventoryUtils.findItemSlot(Items.TOTEM_OF_UNDYING);
        if (slot == -1) return;
        long currentTime = System.currentTimeMillis();
        long d = delay.getValue().longValue();
        if (randomDelay.getValue()) {
            d += random.nextInt(randomization.getValue().intValue() + 1);
        }
        if (currentTime - lastSwap >= d) {
            if (humanize.getValue() && random.nextDouble() < 0.05) return;
            if (onlyOffhand.getValue()) {
                // Move to offhand via inventory interaction (simplified)
                if (mc.getNetworkHandler() != null && mc.player.getInventory() != null) {
                    // Basic hotbar select
                    if (slot < 9) {
                        int prev = mc.player.getInventory().getSelectedSlot();
                        mc.player.getInventory().setSelectedSlot(slot);
                        // Right click would move to offhand in vanilla; context dependent
                        mc.player.getInventory().setSelectedSlot(prev);
                    }
                }
            }
            lastSwap = currentTime;
        }
    }
}
