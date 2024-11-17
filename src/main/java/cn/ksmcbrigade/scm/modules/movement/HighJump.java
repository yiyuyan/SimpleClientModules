package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class HighJump extends Module {

    public HighJump() throws IOException {
        super(HighJump.class.getSimpleName(),false,-1,new Config(new File(HighJump.class.getSimpleName()),get()),ModuleType.MOVEMENT);
    }

    private static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("block",6F);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws IOException {
        getConfig().reload();
    }
}
