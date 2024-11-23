package cn.ksmcbrigade.scm.modules.misc;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class AutoSteal extends Module {

    public AutoSteal() throws IOException {
        super(AutoSteal.class.getSimpleName(),false,-1,new Config(new File(AutoSteal.class.getSimpleName()),get()),ModuleType.MISC);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("wait",1.5F);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
        ModuleUtils.set(AutoStore.class.getSimpleName(),false);
    }
}
