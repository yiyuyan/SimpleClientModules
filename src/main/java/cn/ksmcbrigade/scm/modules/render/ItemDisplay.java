package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scm.utils.ItemInfo;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemDisplay extends Module {

    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("distance",200D);
        object.addProperty("red", 173.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public ItemDisplay() throws IOException {
        super(ItemDisplay.class.getSimpleName(),false,-1,new Config(new File(ItemDisplay.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void renderGame(GuiGraphics pose) throws Exception {
        Minecraft MC = Minecraft.getInstance();
        LocalPlayer player = MC.player;
        if(player==null) return;
        Font font = MC.font;
        Map<String, ItemInfo> map = new HashMap<>();
        for (ItemEntity item : player.level().getEntitiesOfClass(ItemEntity.class, new AABB(player.getPosition(0), player.getPosition(0)).inflate(getConfig().get("distance").getAsDouble()))) {
            if(!map.containsKey(item.getItem().getDescriptionId())){
                map.put(item.getItem().getDescriptionId(),new ItemInfo(item,item.getItem().getCount()));
            }
            else{
                ItemInfo info = map.get(item.getItem().getDescriptionId()).copy();
                info.count+=item.getItem().getCount();
                map.replace(item.getItem().getDescriptionId(),info);
            }
        }
        int i = 0;
        for (ItemInfo info : map.values()) {
            ItemEntity item = info.entity;
            pose.drawString(font,Component.translatable(item.getItem().getDescriptionId()).append(" x"+info.count),2,75+i*9, new Color(getConfig().get("red").getAsFloat()/250f,getConfig().get("green").getAsFloat()/250f,getConfig().get("blue").getAsFloat()/250f,getConfig().get("opacity").getAsFloat()).getRGB(),true);
            i++;
        }
    }
}
