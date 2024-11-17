package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.render.NoWeather;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin {
    @Inject(method = {"isThundering","isRaining"},at = @At("TAIL"),cancellable = true)
    public void is(CallbackInfoReturnable<Boolean> cir){
        Module module = ModuleUtils.get(NoWeather.class.getSimpleName());
        if(module!=null && module.enabled) cir.setReturnValue(false);
    }
}
