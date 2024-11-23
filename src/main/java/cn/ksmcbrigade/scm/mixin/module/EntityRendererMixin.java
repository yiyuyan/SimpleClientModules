package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.render.BigNameTag;
import cn.ksmcbrigade.scm.modules.render.NameTag;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.TriState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @ModifyConstant(method = {"renderNameTag","renderLeash"},constant = @Constant(floatValue = 0.025F))
    public float sc(float constant){
        Module module = ModuleUtils.get(BigNameTag.class.getSimpleName());
        if(module!=null && module.enabled){
            return module.getConfig().get("scale").getAsFloat();
        }
        return constant;
    }

    @Redirect(method = "render",at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/TriState;isDefault()Z"))
    public boolean isDefault(TriState instance){
        return instance.isDefault() || ModuleUtils.enabled(NameTag.class.getSimpleName());
    }

    @Inject(method = "shouldShowName",at = @At(value = "RETURN"),cancellable = true)
    public void showName(T p_114504_, CallbackInfoReturnable<Boolean> cir){
        if(ModuleUtils.enabled(NameTag.class.getSimpleName())) cir.setReturnValue(true);
    }
}
