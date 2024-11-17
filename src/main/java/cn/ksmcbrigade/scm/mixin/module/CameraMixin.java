package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.render.HighCameraDistance;
import cn.ksmcbrigade.scm.modules.render.NoCameraClip;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @ModifyVariable(method = "getMaxZoom", at = @At("HEAD"), index = -1, argsOnly = true)
    public float disa(float value){
        return ModuleUtils.enabled(HighCameraDistance.class.getSimpleName())?ModuleUtils.get(HighCameraDistance.class.getSimpleName()).getConfig().get("distance").getAsFloat():value;
    }

    @Inject(method = "getMaxZoom",at = @At(value = "HEAD"),cancellable = true)
    public void maxZoom(float p_350335_, CallbackInfoReturnable<Float> cir){
        if(ModuleUtils.enabled(NoCameraClip.class.getSimpleName())) cir.setReturnValue(p_350335_);
    }
}
