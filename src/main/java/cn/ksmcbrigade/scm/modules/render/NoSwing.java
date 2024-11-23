package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;

import java.io.IOException;

public class NoSwing extends Module {

    public NoSwing() throws IOException {
        super(NoSwing.class.getSimpleName(),ModuleType.RENDER);
    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) {
        if(event.packet instanceof ServerboundSwingPacket){
            event.setCanceled(true);
        }
    }
}
