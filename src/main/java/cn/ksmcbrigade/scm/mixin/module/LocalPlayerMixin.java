package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.movement.SafeWalk;
import cn.ksmcbrigade.scm.modules.render.InvNether;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow
    @Final
    protected Minecraft minecraft;


    public LocalPlayerMixin(ClientLevel p_250460_, GameProfile p_249912_) {
        super(p_250460_, p_249912_);
    }

    @Unique
    protected boolean isStayingOnGroundSurface() {
        return super.isStayingOnGroundSurface() || ModuleUtils.get(SafeWalk.class.getSimpleName()).enabled;
    }

    @Unique
    protected @NotNull Vec3 maybeBackOffFromEdge(@NotNull Vec3 p_36201_, @NotNull MoverType p_36202_) {
        Vec3 vec3 = super.maybeBackOffFromEdge(p_36201_,p_36202_);
        Module module =ModuleUtils.get(SafeWalk.class.getSimpleName());
        if(module==null) return vec3;
        ((SafeWalk)module).clip(!p_36201_.equals(vec3),this.minecraft,this.minecraft.player);
        return vec3;
    }

    @Redirect(method = "handleConfusionTransitionEffect",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;closeContainer()V"))
    public void close(LocalPlayer instance){
        if(!ModuleUtils.enabled(InvNether.class.getSimpleName())) instance.closeContainer();
    }

    @Redirect(method = "handleConfusionTransitionEffect",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    public void set(Minecraft instance, Screen old){
        if(!ModuleUtils.enabled(InvNether.class.getSimpleName())) instance.setScreen(old);
    }
}
