package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class AutoEat extends Module {

    public int old = -1;
    public boolean d = false;

    public AutoEat() throws IOException {
        super(AutoEat.class.getSimpleName(),false,-1,new Config(new File(AutoEat.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("hungry",8F);
        object.addProperty("heal",8F);
        return object;
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(player==null) return;
        if(player.isCreative()){
            stop();
            return;
        }
        if(player.isUsingItem() || old!=-1){
            stop();
            return;
        }
        if(!(player.getHealth()<=getConfig().get("heal").getAsFloat()) && !(player.getFoodData().getFoodLevel()<=getConfig().get("hungry").getAsFloat())){
            stop();
            return;
        }
        int eat = -1;
        Inventory inventory = player.getInventory();
        for (int i = 0; i < 9; i++) {
            FoodProperties foodProperties = inventory.getItem(i).getFoodProperties(player);
            if(foodProperties!=null){
                if(player.getFoodData().needsFood()){
                    eat = i;
                }
                else{
                    if(foodProperties.canAlwaysEat()){
                        eat = i;
                    }
                }
            }
        }
        if(eat==-1) {
            if(old!=-1){
                inventory.selected = old;
                old = -1;
            }
            return;
        }
        inventory.selected = eat;
        MC.options.keyUse.setDown(true);
        if(MC.gameMode!=null){
            old = inventory.selected;
            inventory.selected = eat;
            MC.gameMode.useItem(player,player.getUsedItemHand());
            new Thread(()->{
                d = true;
                while (d){
                    Thread.yield();
                }
                MC.options.keyUse.setDown(false);
                this.stop();
            }).start();
        }
    }

    public void stop(){
        if(old!=-1){
            Minecraft.getInstance().player.getInventory().selected = old;
            old = -1;
        }
    }
}
