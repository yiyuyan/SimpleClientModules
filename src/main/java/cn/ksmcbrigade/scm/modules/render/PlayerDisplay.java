package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PlayerDisplay extends Module {
    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("position",false);
        object.addProperty("distance",200D);
        object.addProperty("red", 193.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public PlayerDisplay() throws IOException {
        super(PlayerDisplay.class.getSimpleName(),false,-1,new Config(new File(PlayerDisplay.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void renderGame(GuiGraphics pose) throws Exception {
        Minecraft MC = Minecraft.getInstance();
        LocalPlayer player = MC.player;
        if(player==null) return;
        Font font = MC.font;
        int i = 0;
        for (Player item : player.level().getEntitiesOfClass(Player.class, new AABB(player.getPosition(0), player.getPosition(0)).inflate(getConfig().get("distance").getAsDouble()))) {
            if(item==player) continue;
            StringBuilder builder = new StringBuilder(item.getName().getString());
            if(getConfig().get("position").getAsBoolean()){
                Vec3 pos = item.getPosition(0);
                builder.append("(").append(String.format("%.2f",pos.x)).append(",").append(String.format("%.2f",pos.y)).append(",").append(String.format("%.2f",pos.z)).append(")");
            }
            String end = builder.toString();
            pose.drawString(font,end,pose.guiWidth()-font.width(end)-2,pose.guiHeight()-10-i*9, new Color(getConfig().get("red").getAsFloat()/250f,getConfig().get("green").getAsFloat()/250f,getConfig().get("blue").getAsFloat()/250f,getConfig().get("opacity").getAsFloat()).getRGB(),true);
            i++;
        }
    }
}
