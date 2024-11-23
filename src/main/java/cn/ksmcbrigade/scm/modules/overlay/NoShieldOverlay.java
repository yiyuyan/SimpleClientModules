package cn.ksmcbrigade.scm.modules.overlay;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

public class NoShieldOverlay extends Module {

    public NoShieldOverlay() throws IOException {
        super(NoShieldOverlay.class.getSimpleName(),false,-1,new Config(new File(NoShieldOverlay.class.getSimpleName()),get()),ModuleType.OVERLAY);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("blockingOffset",0.5F);
        object.addProperty("commonOffset",0.2F);
        return object;
    }
}
