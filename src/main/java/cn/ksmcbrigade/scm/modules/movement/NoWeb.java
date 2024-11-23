package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NoWeb extends Module {
    public NoWeb() {
        super(NoWeb.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        LocalPlayer player1 =MC.player;
        if(player1==null) return;
        player1.deltaMovementOnPreviousTick = Vec3.ZERO;
    }
}
