package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class bunnyHop extends Module {
    public bunnyHop() {
        super("BunnyHop",ModuleType.MOVEMENT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        LocalPlayer player1 = MC.player;
        if(player1==null) return;
        if(!player1.onGround() || player1.isShiftKeyDown())
            return;

        if(player1.isSprinting() && (player1.input.forwardImpulse>0 || player1.input.leftImpulse>0))
            player1.jumpFromGround();
    }
}
