package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public class NoRotate extends Module {

    public Vec2 XYRot;

    public NoRotate() {
        super(NoRotate.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        if(MC.player!=null){
            XYRot = new Vec2(MC.player.getYRot(),MC.player.getXRot());
        }
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(player==null) return;
        if(XYRot==null){
            XYRot = new Vec2(player.getYRot(),player.getXRot());
        }
    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) {
        if(!(event.packet instanceof ServerboundMovePlayerPacket movePlayerPacket)) return;
        if(XYRot==null) return;
        if(Minecraft.getInstance().player==null) return;
        event.packet = new ServerboundMovePlayerPacket.PosRot(movePlayerPacket.getX(0),movePlayerPacket.getY(0),movePlayerPacket.getZ(0),XYRot.x,XYRot.y,movePlayerPacket.isOnGround());
    }
}
