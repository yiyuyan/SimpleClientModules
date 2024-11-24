package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.movement.AntiEntityPush;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "push*", cancellable = true)
    private void onPushAwayFrom(Entity entity, CallbackInfo ci)
    {
        if(ModuleUtils.enabled(AntiEntityPush.class.getSimpleName())){
            if(entity == Minecraft.getInstance().player){
                ci.cancel();
            }
        }
    }
}
