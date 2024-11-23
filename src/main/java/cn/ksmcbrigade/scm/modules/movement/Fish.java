package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class Fish extends Module {

    public Fish() {
        super(Fish.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if (player != null && (!player.isInWater() || player.isShiftKeyDown())) {
            return;
        }
        Vec3 velocity = null;
        if (player != null) {
            velocity = player.getViewVector(0);
        }
        if (player != null) {
            player.setDeltaMovement(velocity.x,velocity.y+0.005,velocity.z);
        }
    }
}
