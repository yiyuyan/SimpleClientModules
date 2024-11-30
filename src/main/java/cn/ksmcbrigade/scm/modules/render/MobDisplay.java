package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MobDisplay extends Module {

    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("blockMonster",false);
        object.addProperty("blockAnimal",false);
        object.addProperty("distance",200D);
        object.addProperty("red", 173.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public MobDisplay() throws IOException {
        super(MobDisplay.class.getSimpleName(),false,-1,new Config(new File(MobDisplay.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void renderGame(GuiGraphics pose) throws Exception {
        Minecraft MC = Minecraft.getInstance();
        LocalPlayer player = MC.player;
        if(player==null) return;
        Font font = MC.font;
        int height = 75;
        Module module = ModuleUtils.get(ItemDisplay.class.getSimpleName());
        if(module!=null && module.enabled){
            height+=(9*((ItemDisplay)module).syncI+9);
        }
        Map<MutableComponent,Integer> mobMap = new HashMap<>();
        for (Mob mob : player.level().getEntitiesOfClass(Mob.class, new AABB(player.getPosition(0), player.getPosition(0)).inflate(getConfig().get("distance").getAsDouble()))) {
            if((mob instanceof Monster) && getConfig().get("blockMonster").getAsBoolean()) continue;
            if((mob instanceof Animal) && getConfig().get("blockAnimal").getAsBoolean()) continue;
            MutableComponent name = mob.getName().copy();
            if(!mobMap.containsKey(name)){
                mobMap.put(name,1);
            }
            else{
                mobMap.replace(name,mobMap.get(name)+1);
            }
        }
        int i = 0;
        for (MutableComponent mutableComponent : mobMap.keySet()) {
            String count = String.valueOf(mobMap.get(mutableComponent));
            MutableComponent context = mutableComponent.append(" x").append(count);
            pose.drawString(font,context,2,height+i*9,new Color(getConfig().get("red").getAsFloat()/250f,getConfig().get("green").getAsFloat()/250f,getConfig().get("blue").getAsFloat()/250f,getConfig().get("opacity").getAsFloat()).getRGB(),true);
            i++;
        }
    }
}
