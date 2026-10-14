package me.glyph.client.mixin;

import me.glyph.client.GlyphClient;
import me.glyph.client.event.events.UpdateEvent;
import me.glyph.client.event.events.KeyEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onPreTick(CallbackInfo ci) {
        GlyphClient.getInstance().getEventManager().post(new UpdateEvent(true));
    }
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void onPostTick(CallbackInfo ci) {
        GlyphClient.getInstance().getEventManager().post(new UpdateEvent(false));
    }
    
    @Inject(method = "handleKeyBinding", at = @At("HEAD"))
    private void onKey(int key, int scancode, int action, CallbackInfo ci) {
        GlyphClient.getInstance().getEventManager().post(new KeyEvent(key, scancode, action, 0));
    }
}
