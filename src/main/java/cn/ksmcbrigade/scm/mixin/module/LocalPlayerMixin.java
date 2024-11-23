package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.combat.AntiKnockback;
import cn.ksmcbrigade.scm.modules.combat.AutoClick;
import cn.ksmcbrigade.scm.modules.combat.Reach;
import cn.ksmcbrigade.scm.modules.render.RandomName;
import cn.ksmcbrigade.scm.modules.movement.SafeWalk;
import cn.ksmcbrigade.scm.modules.render.InvNether;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow
    @Final
    protected Minecraft minecraft;


    @Shadow public abstract void move(MoverType p_108670_, Vec3 p_108671_);

    @Shadow public abstract void displayClientMessage(Component p_108696_, boolean p_108697_);

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

    @Inject(method = "isHandsBusy",at = @At("RETURN"),cancellable = true)
    public void busy(CallbackInfoReturnable<Boolean> cir){
        Module module = ModuleUtils.get(AutoClick.class.getSimpleName());
        if(module!=null && module.enabled && module.getConfig().get("press").getAsBoolean()){
            cir.setReturnValue(false);
        }
    }

    @Unique
    @Override
    public void setDeltaMovement(double p_20335_, double p_20336_, double p_20337_) {
        double verticalMultiplier = 1F;
        double horizontalMultiplier = 1F;
        Module module = ModuleUtils.get(AntiKnockback.class.getSimpleName());
        if(module!=null && module.enabled){
            verticalMultiplier = 1 - module.getConfig().get("verticalStrength").getAsDouble();
            horizontalMultiplier = 1 - module.getConfig().get("horizontalStrength").getAsDouble();
        }
        super.setDeltaMovement(new Vec3(p_20335_*horizontalMultiplier, p_20336_*verticalMultiplier, p_20337_*horizontalMultiplier));
    }

    @Override
    @Unique
    public Component getDisplayName() {
        if(ModuleUtils.enabled(RandomName.class.getSimpleName())){
            return Component.literal(RandomStringUtils.randomAlphanumeric(4,16));
        }
        return super.getDisplayName();
    }

    @Unique
    @Override
    public double blockInteractionRange() {
        Module module = ModuleUtils.get(Reach.class.getSimpleName());
        if(module!=null && module.enabled){
            return module.getConfig().get("block").getAsDouble();
        }
        return this.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
    }

    @Unique
    @Override
    public double entityInteractionRange() {
        Module module = ModuleUtils.get(Reach.class.getSimpleName());
        if(module!=null && module.enabled){
            return module.getConfig().get("entity").getAsDouble();
        }
        return this.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
    }
}
