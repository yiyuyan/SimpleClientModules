package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.movement.SnowShoe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(method = "canEntityWalkOnPowderSnow",at = @At("RETURN"),cancellable = true)
    private static void canWalk(Entity p_154256_, CallbackInfoReturnable<Boolean> cir){
        if(Minecraft.getInstance().player==null) return;
        if(Minecraft.getInstance().player!=p_154256_) return;
        if(!ModuleUtils.enabled(SnowShoe.class.getSimpleName())) return;
        cir.setReturnValue(true);
    }
}
