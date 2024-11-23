package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.movement.NoSlowDown;
import cn.ksmcbrigade.scm.modules.render.FreeCam;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class ClientPlayerMixin extends Player {

    public ClientPlayerMixin(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(method = "isSpectator",at = @At("RETURN"), cancellable = true)
    public void spectator(CallbackInfoReturnable<Boolean> cir){
        try {
            Module module = ModuleUtils.get(FreeCam.class.getSimpleName());
            if (Minecraft.getInstance().player != null && module!=null && module.enabled && (this.getId() == Minecraft.getInstance().player.getId())) {
                cir.setReturnValue(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Unique
    public boolean isUsingItem(){
        if(ModuleUtils.enabled(NoSlowDown.class.getSimpleName())) return false;
        return super.isUsingItem();
    }
}
