package cn.ksmcbrigade.scm.modules.misc;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class AutoSwitch extends Module {

    public AutoSwitch() {
        super(AutoSwitch.class.getSimpleName(),ModuleType.MISC);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception{
        if(player!=null){
            if(player.getInventory().selected==8){
                player.getInventory().selected=0;
            }
            else{
                player.getInventory().selected++;
            }
        }
    }
}
