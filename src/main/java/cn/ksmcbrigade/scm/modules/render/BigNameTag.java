package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class BigNameTag extends Module {

    public BigNameTag() throws IOException {
        super(BigNameTag.class.getSimpleName(),false,-1,new Config(new File(BigNameTag.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        this.getConfig().reload();
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("scale",0.050F);
        return object;
    }
}
