package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class Blink extends Module {

    public Vec3 pos;
    public Vec2 XYRot;
    public float heal = -999F;

    public Blink() throws IOException {
        super(Blink.class.getSimpleName(),false, KeyEvent.VK_K,new Config(new File(Blink.class.getSimpleName()),get()),ModuleType.MOVEMENT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("blockSend",8D);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        if(MC.player!=null){
            pos = MC.player.getPosition(0);
            XYRot = new Vec2(MC.player.getYRot(),MC.player.getXRot());
            heal = MC.player.getHealth();
        }
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(player==null) return;
        if(pos==null){
            pos = player.getPosition(0);
        }
        if(XYRot==null){
            XYRot = new Vec2(player.getYRot(),player.getXRot());
        }
        if(heal==-999F){
            heal = player.getHealth();
        }
        if(player.getHealth()<heal){
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[Blink] rest the original position because the health down."),true);
            heal = -999F;
            pos = null;
            XYRot = null;
        }
    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) {
        if(!(event.packet instanceof ServerboundMovePlayerPacket movePlayerPacket)) return;
        if(pos==null) return;
        if(XYRot==null) return;
        if(Minecraft.getInstance().player==null) return;
        Vec3 packetPos = Minecraft.getInstance().player.getPosition(0);
        if(packetPos.distanceTo(pos)>getConfig().get("blockSend").getAsDouble()){
            if(MC.player!=null){
                MC.player.displayClientMessage(Component.literal("[Blink] rest the original position because of the distance."),true);
            }
            pos = null;
            XYRot = null;
        }
        else{
            event.packet = new ServerboundMovePlayerPacket.PosRot(pos.x,pos.y,pos.z,XYRot.x,XYRot.y,movePlayerPacket.isOnGround());
        }
    }
}
