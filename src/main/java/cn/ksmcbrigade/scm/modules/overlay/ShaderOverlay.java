package cn.ksmcbrigade.scm.modules.overlay;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.io.File;
import java.io.IOException;

public class ShaderOverlay extends Module {

    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("red", 173.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public ShaderOverlay() throws IOException {
        super(ShaderOverlay.class.getSimpleName(),false,-1,new Config(new File(ShaderOverlay.class.getSimpleName()),get()),ModuleType.OVERLAY);
    }

    @Override
    public void renderLevel(Minecraft mc, RenderLevelStageEvent event) {
        RenderSystem.setShaderColor(getConfig().get("red").getAsFloat()/255f,getConfig().get("green").getAsFloat()/255f,getConfig().get("blue").getAsFloat()/255f,getConfig().get("opacity").getAsFloat());
    }
}
