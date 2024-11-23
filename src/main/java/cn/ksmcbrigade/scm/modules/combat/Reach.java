package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class Reach extends Module {

    public Reach() throws IOException {
        super(Reach.class.getSimpleName(),false,-1,new Config(new File(Reach.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("block",5D);
        object.addProperty("entity",5D);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
    }
}
