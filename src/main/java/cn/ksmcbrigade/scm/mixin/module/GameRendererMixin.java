package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.overlay.NoHandOverlay;
import cn.ksmcbrigade.scm.modules.overlay.NoNauseaOverlay;
import cn.ksmcbrigade.scm.modules.render.NoHurtCam;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
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
        Module module = ModuleUtils.get(NoHandOverlay.class.getSimpleName());
        if(module!=null && module.enabled){
            ci.cancel();
        }
    }

    @Inject(method = "bobHurt",at = @At("HEAD"),cancellable = true)
    public void renderHurt(PoseStack p_109118_, float p_109119_, CallbackInfo ci){
        Module module = ModuleUtils.get(NoHurtCam.class.getSimpleName());
        if(module!=null && module.enabled){
            ci.cancel();
        }
    }

    @Inject(method = "renderConfusionOverlay",at = @At("HEAD"),cancellable = true)
    public void renderHurt(GuiGraphics p_282460_, float p_282656_, CallbackInfo ci){
        Module module = ModuleUtils.get(NoNauseaOverlay.class.getSimpleName());
        if(module!=null && module.enabled){
            ci.cancel();
        }
    }
}
