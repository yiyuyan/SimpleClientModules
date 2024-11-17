package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Spider extends Module {
    public Spider() {
        super(Spider.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(player==null) return;
        if(!player.horizontalCollision) return;
        Vec3 vec3 = player.getViewVector(0);
        if(vec3.y>=0.2) return;
        player.setDeltaMovement(0,0.2,0);
    }
}
