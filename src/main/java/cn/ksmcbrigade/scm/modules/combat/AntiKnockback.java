package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

public class AntiKnockback extends Module {
    public AntiKnockback() throws IOException {
        super(AntiKnockback.class.getSimpleName(),false,-1,new Config(new File(AntiKnockback.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("verticalStrength",1d);
        object.addProperty("horizontalStrength",1d);
        return object;
    }
}
