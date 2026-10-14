package me.glyph.client.gui;

import me.glyph.client.GlyphClient;
import me.glyph.client.module.Category;
import me.glyph.client.module.Module;
import me.glyph.client.module.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private int x = 100;
    private int y = 100;
    private int width = 100;
    private int height = 14;
    private boolean dragging = false;
    private int dragX, dragY;
    private int scroll = 0;
    
    private Category selectedCategory = Category.COMBAT;
    private Module selectedModule = null;
    
    public ClickGui() {
        super(Text.literal("Glyph Client ClickGUI"));
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int currentY = y + 15;
        for (Category category : Category.values()) {
            int color = selectedCategory == category ? 0xFF00AAFF : 0xFFFFFFFF;
            context.drawText(textRenderer, category.getName(), x, currentY, color, false);
            currentY += 12;
        }
        currentY = y + 15;
        int startX = x + width + 5;
        for (Module module : GlyphClient.getInstance().getModuleManager().getModulesByCategory(selectedCategory)) {
            if (currentY > y + height + 200) break;
            int color = module.isEnabled() ? 0xFF00FF00 : 0xFFFFFFFF;
            context.drawText(textRenderer, module.getName(), startX, currentY, color, false);
            currentY += 12;
        }
        if (selectedModule != null) {
            int sX = startX + 110;
            currentY = y + 15;
            for (Setting<?> setting : selectedModule.getSettings()) {
                String text = setting.getName() + ": ";
                if (setting.isBoolean()) {
                    text += setting.getValue().toString();
                } else if (setting.isNumber()) {
                    text += setting.getValue().toString();
                } else if (setting.isMode()) {
                    text += setting.getValue().toString();
                }
                context.drawText(textRenderer, text, sX, currentY, 0xFFFFFFFF, false);
                currentY += 12;
            }
        }
        context.drawText(textRenderer, "Glyph Client", x, y, 0xFF00AAFF, true);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
