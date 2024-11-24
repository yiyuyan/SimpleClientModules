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
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class AutoSword extends Module {

    public AutoSword() throws IOException {
        super(AutoSword.class.getSimpleName(),false,-1,new Config(new File(AutoSword.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("blockFlights",true);
        object.addProperty("blockAnimals",false);
        object.addProperty("blockMonsters",false);
        object.addProperty("blockPlayers",false);
        object.addProperty("blockSleeping",true);
        object.addProperty("livingOnly",false);
        return object;
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(player==null) return;
        if(MC.hitResult==null) return;
        if(MC.hitResult.getType()!= HitResult.Type.ENTITY) return;
        Entity entity = ((EntityHitResult)MC.hitResult).getEntity();
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
        int sword = -1;
        for (int i = 0; i < 9; i++) {
            if(player.getInventory().getItem(i).getItem() instanceof SwordItem){
                if(sword==-1){
                    sword = i;
                }
                else{
                    if(thanLast(player.getInventory().getItem(i).getItem(),player.getInventory().getItem(sword).getItem())){
                        sword = i;
                    }
                }
            }
        }
        if(sword==-1) return;
        player.getInventory().selected = sword;
    }

    public static boolean thanLast(Item now,Item last){
        Tier tier = ((SwordItem)now).getTier();
        Tier lastTier = ((SwordItem)last).getTier();
        if(tier.equals(Tiers.NETHERITE)){
            return true;
        }
        else if(tier.equals(Tiers.DIAMOND) && lastTier.equals(Tiers.NETHERITE)){
            return false;
        }
        else if(tier.equals(Tiers.IRON) && (lastTier.equals(Tiers.NETHERITE) || lastTier.equals(Tiers.DIAMOND))){
            return false;
        }
        else if(tier.equals(Tiers.STONE) && (lastTier.equals(Tiers.NETHERITE) || lastTier.equals(Tiers.DIAMOND) || lastTier.equals(Tiers.IRON))){
            return false;
        }
        else if(tier.equals(Tiers.GOLD) && (lastTier.equals(Tiers.NETHERITE) || lastTier.equals(Tiers.DIAMOND) || lastTier.equals(Tiers.IRON) || lastTier.equals(Tiers.STONE))){
            return false;
        }
        else return !tier.equals(Tiers.GOLD) && !tier.equals(Tiers.WOOD);
    }
}
