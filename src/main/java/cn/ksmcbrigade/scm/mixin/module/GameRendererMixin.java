package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.overlay.NoHnadOverlay;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderItemInHand",at = @At("HEAD"),cancellable = true)
    public void renderHand(Camera p_109122_, float p_109123_, Matrix4f p_333953_, CallbackInfo ci){
        Module module = ModuleUtils.get(NoHnadOverlay.class.getSimpleName());
        if(module!=null && module.enabled){
            ci.cancel();
        }
    }
}
