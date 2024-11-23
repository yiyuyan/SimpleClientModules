package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FastLadder extends Module {
    public FastLadder() {
        super(FastLadder.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        LocalPlayer player1 =MC.player;
        if(player1==null) return;
        if(!player1.onClimbable() || !player1.horizontalCollision)
            return;

        if(player1.input.forwardImpulse == 0
                && player1.input.leftImpulse == 0)
            return;

        Vec3 velocity = player1.getViewVector(0);
        player1.setDeltaMovement(velocity.x, 0.2872, velocity.z);
    }
}
