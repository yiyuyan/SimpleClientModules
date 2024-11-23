package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class AutoReconnect extends Module {

    public AutoReconnect() throws IOException {
        super(AutoReconnect.class.getSimpleName(),false,-1,new Config(new File(AutoReconnect.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("waitSecond",5);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
    }
}
