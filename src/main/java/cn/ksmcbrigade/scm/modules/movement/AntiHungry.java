package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class AntiHungry extends Module {
    public AntiHungry() {
        super(AntiHungry.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) throws Exception {
        if(!(event.packet instanceof ServerboundMovePlayerPacket last)) return;
        if(MC.player==null) return;
        if(!MC.player.onGround() || MC.player.fallDistance>0.5) return;
        if(MC.gameMode==null) return;
        if(MC.gameMode.isDestroying()) return;
        double x = last.getX(-1);
        double y = last.getY(-1);
        double z = last.getZ(-1);
        float yaw = last.getYRot(-1);
        float pitch = last.getXRot(-1);
        boolean onGround = last.isOnGround();

        Packet<?> newPacket;
        if(last.hasPosition())
            if(last.hasRotation())
                newPacket = new ServerboundMovePlayerPacket.PosRot(x, y, z, yaw, pitch,
                        onGround);
            else
                newPacket = new ServerboundMovePlayerPacket.Pos(x, y, z,
                        onGround);
        else if(last.hasRotation())
            newPacket = new ServerboundMovePlayerPacket.Rot(yaw, pitch, onGround);
        else
            newPacket = new ServerboundMovePlayerPacket.StatusOnly(onGround);

        event.packet = newPacket;
    }
}
