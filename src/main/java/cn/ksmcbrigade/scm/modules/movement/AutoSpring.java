package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class AutoSpring extends Module {

    private boolean last = false;

    public AutoSpring() {
        super(AutoSpring.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void enabled(Minecraft MC) {
        last = false;
        if(MC.player!=null){
            last = MC.player.isSprinting();
        }
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(MC.player!=null){
            MC.player.setSprinting(true);
        }
    }

    @Override
    public void disabled(Minecraft MC) {
        if(MC.player!=null){
            MC.player.setSprinting(last);
        }
    }
}
