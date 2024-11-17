package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class BoatFly extends Module {
    public BoatFly() throws IOException {
        super(BoatFly.class.getSimpleName(),ModuleType.MOVEMENT);
        JsonObject object = new JsonObject();
        object.addProperty("movement",0.3D);
        setConfig(new Config(new File("BoatFly"),object));
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(player==null){
            return;
        }
        if(player.getVehicle()==null){
            return;
        }
        Entity vehicle = player.getVehicle();
        if(!vehicle.getType().equals(EntityType.BOAT)){
            return;
        }
        if(!MC.options.keyJump.isDown()){
            return;
        }
        JsonElement dm = getConfig().get("movement");
        vehicle.setDeltaMovement(new Vec3(0,dm==null?0.3D:dm.getAsDouble(),0));
    }
}
