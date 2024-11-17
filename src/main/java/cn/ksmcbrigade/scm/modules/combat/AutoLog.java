package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class AutoLog extends Module {

    public AutoLog() throws IOException {
        super(AutoLog.class.getSimpleName(),false, -1,new Config(new File(AutoLog.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("heal",4D);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception{
        if(player!=null && MC.getConnection()!=null && player.getHealth()<=getConfig().get("heal").getAsDouble() && (!ModuleUtils.get(AutoTotem.class.getSimpleName()).enabled || ((AutoTotem)ModuleUtils.get(AutoTotem.class.getSimpleName())).getTotem()<=0)){
            MC.getConnection().disconnect(Component.literal("AutoLog"));
            this.setEnabled(false);
        }
    }
}
