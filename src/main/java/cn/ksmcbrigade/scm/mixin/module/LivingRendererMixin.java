package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.render.NameTag;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingRendererMixin {

    @Inject(method = "shouldShowName*",at = @At(value = "RETURN"),cancellable = true)
    public void showName(CallbackInfoReturnable<Boolean> cir){
        if(ModuleUtils.enabled(NameTag.class.getSimpleName())) cir.setReturnValue(true);
    }
}
