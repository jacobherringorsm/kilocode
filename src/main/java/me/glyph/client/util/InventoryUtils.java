package me.glyph.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static int findItemSlot(Item item) {
        if (mc.player == null) return -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findItemSlotHotbar(Item item) {
        return findItemSlot(item);
    }
    
    public static boolean hasItem(Item item) {
        return findItemSlot(item) != -1;
    }
    
    public static void swapToSlot(int slot) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        if (slot >= 0 && slot < 9) {
            mc.player.getInventory().setSelectedSlot(slot);
        }
    }
}
