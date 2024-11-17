package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.BuiltInModules.render.RainbowGui;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class FPS extends Module {
    public FPS() {
        super(FPS.class.getSimpleName(), ModuleType.RENDER);
    }

    @Override
    public void renderGame(GuiGraphics pose) {
        Minecraft MC = Minecraft.getInstance();

        if(MC.player==null) return;

        int color = 0;
        if(!ModuleUtils.enabled("RainbowGui")){
            color = Color.WHITE.getRGB();
        }
        else{
            color = RainbowGui.getColor().get("c").getAsInt();
        }

        int i = 1;
        if(ModuleUtils.enabled(XYZ.class.getSimpleName()) && !((XYZ)ModuleUtils.get(XYZ.class.getSimpleName())).shift) i++;
        if(ModuleUtils.enabled(Ping.class.getSimpleName())) i++;

        String xyz = "Fps: ";
        try{
            xyz+=MC.getFps();
        }
        catch (Exception e){
            xyz+="-1";
        }
        pose.drawString(MC.font,xyz,0,MC.getWindow().getGuiScaledHeight()-i*MC.font.lineHeight,color);
    }
}
