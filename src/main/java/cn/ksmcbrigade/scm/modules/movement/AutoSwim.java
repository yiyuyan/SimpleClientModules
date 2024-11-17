package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class AutoSwim extends Module {

    public AutoSwim() {
        super(AutoSwim.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception{
        if(player==null){
            return;
        }
        if(player.horizontalCollision || MC.options.keyShift.isDown()){
            return;
        }
        if(!player.isInWater()){
            return;
        }
        if(player.getSwimAmount(0)>0){
            player.setSprinting(true);
        }
    }
}
