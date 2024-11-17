package cn.ksmcbrigade.scm.modules.block;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class AutoMine extends Module {

    private boolean last = false;

    public AutoMine() {
        super(AutoMine.class.getSimpleName(),ModuleType.BLOCK);
    }

    @Override
    public void enabled(Minecraft MC) {
        last = MC.options.keyAttack.isDown();
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        KeyMapping.set(MC.options.keyAttack.getKey(),true);
        MC.options.keyAttack.setDown(true);
    }

    @Override
    public void disabled(Minecraft MC) {
        MC.options.keyAttack.setDown(last);
    }
}
