package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class TpAura extends Module {

    public int wait;

    public TpAura() throws IOException {
        super(TpAura.class.getSimpleName(),false, -1,new Config(new File(TpAura.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("reach",5D);
        object.addProperty("wait",15);
        object.addProperty("blockFlights",true);
        object.addProperty("blockAnimals",false);
        object.addProperty("blockMonsters",false);
        object.addProperty("blockPlayers",false);
        object.addProperty("blockSleeping",true);
        object.addProperty("secondary",false);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
        wait = getConfig().get("wait").getAsInt();
        ModuleUtils.set("KillAura",false);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player)  throws Exception{
        if(wait!=0){
            wait--;
            return;
        }
        if(player==null) return;

            LivingEntity entity = player.level().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT.range(getConfig().get("reach").getAsDouble()),player,player.getX(),player.getY(),player.getZ(),new AABB(player.getPosition(0),player.getPosition(0)).inflate(getConfig().get("reach").getAsDouble()));
            if(entity==null) return;
            if(!entity.isAttackable()){
                return;
            }
            if(entity.isInvulnerable()){
                return;
            }
            if(entity instanceof Animal && getConfig().get("blockAnimals").getAsBoolean()){
                return;
            }
            if(entity instanceof Monster && getConfig().get("blockMonsters").getAsBoolean()){
                return;
            }
            if(entity instanceof Player && getConfig().get("blockPlayers").getAsBoolean()){
                return;
            }
            if((entity.isFallFlying() || (entity instanceof Player player1 && player1.getAbilities().flying)) && getConfig().get("blockFlights").getAsBoolean()){
                return;
            }
            if(entity.isSleeping() && getConfig().get("blockSleeping").getAsBoolean()){
                return;
            }
            if(MC.getConnection()==null){
                return;
            }
            if(player.distanceTo(entity)<=10){
                player.setPos(entity.getPosition(0));
                player.moveTo(entity.getPosition(0));
                MC.getConnection().getConnection().send(new ServerboundMovePlayerPacket.Pos(entity.getX(),entity.getY(),entity.getZ(),player.onGround()));
            }
            MC.getConnection().getConnection().send(ServerboundInteractPacket.createAttackPacket(entity,getConfig().get("secondary").getAsBoolean()));
            wait = getConfig().get("wait").getAsInt();
    }
}
