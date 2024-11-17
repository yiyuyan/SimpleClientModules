package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class FreeCam extends Module {

    public Vec3 pos;
    public float speed = 0.05F;

    public FreeCam() {
        super(FreeCam.class.getSimpleName(),false, KeyEvent.VK_U,ModuleType.RENDER);
    }

    @Override
    public void enabled(Minecraft MC) throws IOException {
        if(MC.player!=null && MC.level!=null){
            this.pos = MC.player.getPosition(0);
            this.speed = MC.player.getAbilities().getFlyingSpeed();
            MC.player.getAbilities().flying = true;
            MC.level.sendPacketToServer(new ServerboundPlayerAbilitiesPacket(MC.player.getAbilities()));
        }
        if(getConfig()==null){
            JsonObject config = new JsonObject();
            config.addProperty("speed",-1.0F);
            config.addProperty("up",0.5D);
            setConfig(new Config(new File("FreeCam"),config));
        }

        getConfig().reload();
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(player!=null && MC.cameraEntity!=null){

            JsonElement sp = getConfig().get("speed");
            if(sp!=null && sp.getAsFloat()!=-1.0F){
                player.getAbilities().setFlyingSpeed(sp.getAsFloat());
            }

            player.getAbilities().flying = true;
            player.setOnGround(false);

            JsonElement up = getConfig().get("up");
            if(MC.options.keyJump.isDown())
                player.setDeltaMovement(0, up==null?0.5d:up.getAsDouble(), 0);

            if(MC.options.keyShift.isDown())
                player.setDeltaMovement(0,-(up==null?0.5d:up.getAsDouble()),0);
        }
    }

    @Override
    public void disabled(Minecraft MC) {
        if(pos!=null && MC.player!=null && MC.level!=null){
            MC.player.setPos(pos);
            if(!MC.isSingleplayer()){
                MC.level.sendPacketToServer(new ServerboundMovePlayerPacket.Pos(pos.x,pos.y,pos.z,true));
            }
            else {
                MC.player.moveTo(pos);
            }
            MC.player.getAbilities().setFlyingSpeed(this.speed);
            MC.player.getAbilities().flying = false;
            MC.level.sendPacketToServer(new ServerboundPlayerAbilitiesPacket(MC.player.getAbilities()));
        }
    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) {
        if(event.packet instanceof ServerboundMovePlayerPacket){
            event.setCanceled(true);
        }
    }
}
