package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class AntiProjectiles extends Module {

    public int wait;

    public AntiProjectiles() throws IOException {
        super(AntiProjectiles.class.getSimpleName(),false, -1,new Config(new File(AntiProjectiles.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("reach",5D);
        object.addProperty("wait",15);
        object.addProperty("blockWitherSkulls",true);
        object.addProperty("blockFireBalls",false);
        object.addProperty("blockDragonBalls",false);
        object.addProperty("blockWindCharges",false);
        object.addProperty("blockSnowBalls",true);
        object.addProperty("secondary",false);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
        wait = getConfig().get("wait").getAsInt();
        ModuleUtils.set("TpAura",false);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception{
        if(wait!=0){
            wait--;
            return;
        }
        if(player==null) return;

            for (Projectile entity : player.level().getEntitiesOfClass(Projectile.class, new AABB(player.getPosition(0), player.getPosition(0)).inflate(getConfig().get("reach").getAsDouble()))) {
                if(entity==null) return;
                if(!entity.isAttackable()){
                    return;
                }
                if(entity.isInvulnerable()){
                    return;
                }
                if(entity instanceof WitherSkull && getConfig().get("blockWitherSkulls").getAsBoolean()){
                    return;
                }
                if(entity instanceof Fireball && getConfig().get("blockFireBalls").getAsBoolean()){
                    return;
                }
                if(entity instanceof DragonFireball && getConfig().get("blockDragonBalls").getAsBoolean()){
                    return;
                }
                if(entity instanceof WindCharge && getConfig().get("blockWindCharges").getAsBoolean()){
                    return;
                }
                if(entity instanceof Snowball && getConfig().get("blockSnowBalls").getAsBoolean()){
                    return;
                }
                if(MC.getConnection()==null){
                    return;
                }
                MC.getConnection().getConnection().send(ServerboundInteractPacket.createAttackPacket(entity,getConfig().get("secondary").getAsBoolean()));
            }
        wait = getConfig().get("wait").getAsInt();
    }
}
