package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.render.FreeCam;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {

    @Shadow @Final private GameProfile profile;

    @Inject(method = "getGameMode",at = @At("RETURN"), cancellable = true)
    public void spectator(CallbackInfoReturnable<Boolean> cir){
        try {
            Module module = ModuleUtils.get(FreeCam.class.getSimpleName());
            if (Minecraft.getInstance().player != null && module!=null && module.enabled && (this.profile.getId() == Minecraft.getInstance().getUser().getProfileId())) {
                cir.setReturnValue(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
