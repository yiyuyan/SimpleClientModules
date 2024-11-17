package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.render.NoWeather;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = {"isThundering","isRaining"},at = @At("TAIL"),cancellable = true)
    public void isRain(CallbackInfoReturnable<Boolean> cir){
        Module module = ModuleUtils.get(NoWeather.class.getSimpleName());
        if(module!=null && module.enabled) cir.setReturnValue(false);
    }

    @Inject(method = {"getRainLevel","getThunderLevel"},at = @At("TAIL"),cancellable = true)
    public void is(CallbackInfoReturnable<Float> cir){
        Module module = ModuleUtils.get(NoWeather.class.getSimpleName());
        if(module!=null && module.enabled) cir.setReturnValue(0F);
    }
}
