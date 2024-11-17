package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class HighCameraDistance extends Module {

    public HighCameraDistance() throws IOException {
        super(HighCameraDistance.class.getSimpleName(),false,-1,new Config(new File(HighCameraDistance.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        this.getConfig().reload();
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("distance",12.0F);
        return object;
    }
}
