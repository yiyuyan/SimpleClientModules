package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

import java.io.IOException;

public class Criticals extends Module {

    public Criticals() throws IOException {
        super(Criticals.class.getSimpleName(),ModuleType.COMBAT);
    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) throws Exception {
        if(MC.player==null) return;
        if(MC.getConnection()==null) return;
        if(event.packet instanceof ServerboundInteractPacket packet){
            if(packet.action==ServerboundInteractPacket.ATTACK_ACTION){
                if(!MC.player.onGround()) return;
                if(MC.player.isInWater() || MC.player.isInLava()) return;
                MC.getConnection().send(new ServerboundMovePlayerPacket.Pos(MC.player.getX(),MC.player.getY()+0.0625,MC.player.getZ(),true));
                MC.getConnection().send(new ServerboundMovePlayerPacket.Pos(MC.player.getX(),MC.player.getY(),MC.player.getZ(),false));
                MC.getConnection().send(new ServerboundMovePlayerPacket.Pos(MC.player.getX(),MC.player.getY()+1.1e-5,MC.player.getZ(),false));
                MC.getConnection().send(new ServerboundMovePlayerPacket.Pos(MC.player.getX(),MC.player.getY(),MC.player.getZ(),false));
            }
        }
    }
}
