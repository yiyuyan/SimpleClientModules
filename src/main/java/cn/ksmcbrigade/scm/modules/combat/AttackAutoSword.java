package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import static cn.ksmcbrigade.scm.modules.combat.AutoSword.thanLast;

public class AttackAutoSword extends Module {

    public AttackAutoSword() throws IOException {
        super(AttackAutoSword.class.getSimpleName(),false,-1,new Config(new File(AttackAutoSword.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("blockFlights",true);
        object.addProperty("blockAnimals",false);
        object.addProperty("blockMonsters",false);
        object.addProperty("blockPlayers",false);
        object.addProperty("blockSleeping",true);
        object.addProperty("blockTeam",true);
        object.addProperty("livingOnly",false);
        return object;
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(player==null) return;
        if(MC.hitResult==null) return;
        if(MC.hitResult.getType()!= HitResult.Type.ENTITY) return;
        Entity entity = ((EntityHitResult)MC.hitResult).getEntity();

    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) throws Exception {
        if(MC.player==null) return;
        if(MC.level==null) return;
        if(event.packet instanceof ServerboundInteractPacket packet){
            if(packet.action==ServerboundInteractPacket.ATTACK_ACTION){
                Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
                if(entity==null) return;
                if(!entity.isAttackable()){
                    return;
                }
                if(entity.isInvulnerable()){
                    return;
                }
                if(!(entity instanceof LivingEntity) && getConfig().get("livingOnly").getAsBoolean()) return;
                switch (entity) {
                    case Animal animal when getConfig().get("blockAnimals").getAsBoolean() -> {
                        return;
                    }
                    case Monster monster when getConfig().get("blockMonsters").getAsBoolean() -> {
                        return;
                    }
                    case Player player1 when getConfig().get("blockPlayers").getAsBoolean() -> {
                        return;
                    }
                    default -> {
                    }
                }
                if(entity instanceof LivingEntity living){
                    if((living.isFallFlying() || (living instanceof Player player1 && player1.getAbilities().flying)) && getConfig().get("blockFlights").getAsBoolean()){
                        return;
                    }
                    if(living.isSleeping() && getConfig().get("blockSleeping").getAsBoolean()){
                        return;
                    }
                }
                if(entity.getTeam()!=null && MC.player.getTeam()!=null && entity.getTeam().equals(MC.player.getTeam()) && getConfig().get("blockTeam").getAsBoolean()) return;
                int sword = -1;
                for (int i = 0; i < 9; i++) {
                    Item item = MC.player.getInventory().getItem(i).getItem();
                    if((item instanceof SwordItem) || (item instanceof AxeItem)){
                        if(sword==-1){
                            sword = i;
                        }
                        else{
                            if(thanLast(MC.player.getInventory().getItem(i).getItem(),MC.player.getInventory().getItem(sword).getItem())){
                                sword = i;
                            }
                        }
                    }
                }
                if(sword==-1) return;
                MC.player.getInventory().selected = sword;
            }

        }
    }
}
