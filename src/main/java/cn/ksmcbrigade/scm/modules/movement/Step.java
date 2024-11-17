package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Step extends Module {

    public Step() throws IOException {
        super(Step.class.getSimpleName(),false,-1,new Config(new File(Step.class.getSimpleName()),get()),ModuleType.MOVEMENT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("block",1.5F);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws IOException {
        getConfig().reload();
        JsonElement e = getConfig().get("block");
        if(MC.player!=null){
            Objects.requireNonNull(MC.player.getAttribute(Attributes.STEP_HEIGHT)).setBaseValue(e==null?1f:e.getAsFloat());
            Objects.requireNonNull(MC.player.getAttribute(Attributes.STEP_HEIGHT)).dirty = false;
            Objects.requireNonNull(MC.player.getAttribute(Attributes.STEP_HEIGHT)).cachedValue = (e==null?1f:e.getAsFloat());
        }
    }

    @Override
    public void disabled(Minecraft MC) {
        if(MC.player!=null){
            Objects.requireNonNull(MC.player.getAttribute(Attributes.STEP_HEIGHT)).setBaseValue(0.5F);
            Objects.requireNonNull(MC.player.getAttribute(Attributes.STEP_HEIGHT)).dirty = false;
            Objects.requireNonNull(MC.player.getAttribute(Attributes.STEP_HEIGHT)).cachedValue = 0.5F;
        }
    }
}
