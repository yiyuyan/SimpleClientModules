package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.movement.NoSlowDown;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "getSpeedFactor",at = @At("RETURN"),cancellable = true)
    public void get(CallbackInfoReturnable<Float> cir){
        if(cir.getReturnValue()<1F && ModuleUtils.enabled(NoSlowDown.class.getSimpleName())){
            cir.setReturnValue(1F);
        }
    }
}
