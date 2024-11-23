package cn.ksmcbrigade.scm.modules.block;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class FastMine extends Module {

    public FastMine() {
        super(FastMine.class.getSimpleName(),ModuleType.BLOCK);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if (Minecraft.getInstance().gameMode != null) {
            Minecraft.getInstance().gameMode.destroyDelay = 0;
        }
    }
}
