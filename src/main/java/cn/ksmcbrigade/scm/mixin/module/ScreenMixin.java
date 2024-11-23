package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.overlay.NoBackground;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = {"renderTransparentBackground","renderBackground"},at = @At("HEAD"), cancellable = true)
    public void render(CallbackInfo ci){
        if(ModuleUtils.enabled(NoBackground.class.getSimpleName()) && NoBackground.renderCancel()){
            ci.cancel();
        }
    }
}
