package cn.ksmcbrigade.scm.mixin.fix;

import cn.ksmcbrigade.scb.guis.configuration.ConfigList;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scm.modules.render.XRay;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfigList.StringEntry.class)
public class FixXRayConfig {

    @Unique
    private boolean xray = false;

    @Inject(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setCanLoseFocus(Z)V",shift = At.Shift.BEFORE))
    public void init(Module _module, ConfigList list, boolean setModule, String key, CallbackInfo ci){
        if(_module.getName().equalsIgnoreCase(XRay.class.getSimpleName())){
            xray = true;
        }
    }

    @Redirect(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setCanLoseFocus(Z)V"))
    public void focus(EditBox instance,boolean value){
        instance.setCanLoseFocus(value);
        if(xray){
            instance.setBordered(true);
            instance.setWidth(instance.getWidth()*4-8);
            instance.setMaxLength(2048);
        }
    }
}
