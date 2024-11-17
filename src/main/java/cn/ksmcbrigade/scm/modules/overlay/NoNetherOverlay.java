package cn.ksmcbrigade.scm.modules.overlay;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class NoNetherOverlay extends Module {

    public NoNetherOverlay() {
        super(NoNetherOverlay.class.getSimpleName(),ModuleType.OVERLAY);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(player instanceof LocalPlayer localPlayer){
            localPlayer.spinningEffectIntensity = 0f;
            localPlayer.oSpinningEffectIntensity = 0f;
        }
    }
}
