package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class NoGravity extends Module {

    public NoGravity() {
        super(NoGravity.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(MC.player!=null && MC.cameraEntity!=null){
            MC.player.setNoGravity(true);
            MC.cameraEntity.setNoGravity(true);
        }
    }

    @Override
    public void disabled(Minecraft MC) throws Exception {
        if(MC.player!=null && MC.cameraEntity!=null){
            MC.player.setNoGravity(false);
            MC.cameraEntity.setNoGravity(false);
        }
    }
}
