package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.BuiltInModules.render.RainbowGui;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class XYZ extends Module {

    public boolean shift = false;

    public XYZ() {
        super(XYZ.class.getSimpleName(), ModuleType.RENDER);
    }

    @Override
    public void renderGame(GuiGraphics pose) {
        Minecraft MC = Minecraft.getInstance();

        if(MC.options.keyShift.isDown()){
            shift = true;
            return;
        }

        shift = false;

        if(MC.player==null) return;

        int color = 0;
        if(!ModuleUtils.enabled("RainbowGui")){
            color = Color.WHITE.getRGB();
        }
        else{
            color = RainbowGui.getColor().get("c").getAsInt();
        }

        String xyz = "XYZ: " + Math.round(MC.player.getX()*100.0)/100.0D + ", " + Math.round(MC.player.getY()*100.0)/100.0D + ", " + Math.round(MC.player.getZ()*100.0)/100.0D;
        pose.drawString(MC.font,xyz,0,MC.getWindow().getGuiScaledHeight()-MC.font.lineHeight,color);
    }
}
