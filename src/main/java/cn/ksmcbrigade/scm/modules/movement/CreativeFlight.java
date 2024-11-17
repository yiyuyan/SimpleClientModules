package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.util.Objects;

public class CreativeFlight extends Module {

    private boolean last = false;
    private boolean set = false;

    public CreativeFlight() {
        super(CreativeFlight.class.getSimpleName(),false, KeyEvent.VK_G,ModuleType.MOVEMENT);
    }

    @Override
    public void enabled(Minecraft MC) {
        last = false;
        if(MC.player!=null){
            last =  MC.player.mayFly();
        }
        if(MC.player!=null && MC.getConnection()!=null){
            MC.player.getAbilities().mayfly = true;
            Objects.requireNonNull(MC.player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)).setBaseValue(1);
            MC.getConnection().getConnection().send(new ServerboundPlayerAbilitiesPacket(MC.player.getAbilities()));
            set = true;
        }
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(MC.player==null) return;
        if(MC.getConnection()==null) return;
        if(!set){
            MC.player.getAbilities().mayfly = true;
            Objects.requireNonNull(MC.player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)).setBaseValue(1);
            MC.getConnection().getConnection().send(new ServerboundPlayerAbilitiesPacket(MC.player.getAbilities()));
            set = true;
        }
        MC.player.getAbilities().mayfly = true;
        Objects.requireNonNull(MC.player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)).setBaseValue(1);
    }

    @Override
    public void disabled(Minecraft MC) {
        if(MC.player==null) return;
        if(MC.getConnection()==null) return;
        if(MC.player.isCreative() || MC.player.isSpectator()) return;
        if(last) return;
        MC.player.getAbilities().mayfly = false;
        MC.player.getAbilities().flying = false;
        Objects.requireNonNull(MC.player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)).setBaseValue(0);
        MC.getConnection().getConnection().send(new ServerboundPlayerAbilitiesPacket(MC.player.getAbilities()));
        set=last=false;
    }
}
