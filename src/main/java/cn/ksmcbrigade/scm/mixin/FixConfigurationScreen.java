package cn.ksmcbrigade.scm.mixin;

import cn.ksmcbrigade.scb.guis.configuration.ConfigList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ConfigList.BooleanEntry.class)
public class FixConfigurationScreen {
    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Checkbox;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/Font;)Lnet/minecraft/client/gui/components/Checkbox$Builder;"),index = 0)
    public Component init(Component p_309029_){
        return Component.literal("");
    }
}
