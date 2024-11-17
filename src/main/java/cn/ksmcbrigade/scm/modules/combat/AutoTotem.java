package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class AutoTotem extends Module {

    public int last = -1;

    public AutoTotem() throws IOException {
        super(AutoTotem.class.getSimpleName(),ModuleType.COMBAT);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception{
        if(player!=null){
            if(last!=-1){
                Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundPickItemPacket(last));
                last = -1;
            }
            if(player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)){
                return;
            }
            if(!player.getOffhandItem().isEmpty()){
                return;
            }
            for(int i=0;i<player.getInventory().getMaxStackSize();i++){
                if(player.getInventory().getItem(i).getItem().equals(Items.TOTEM_OF_UNDYING)){
                    if(i<9){
                        final int last = player.getInventory().selected;
                        player.getInventory().selected = i;
                        Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                        player.getInventory().selected = last;
                        //MC.getConnection().getConnection().send(new ServerboundSetCarriedItemPacket(last));
                    }
                    else{
                        Objects.requireNonNull(MC.getConnection()).getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                        MC.getConnection().getConnection().send(new ServerboundPickItemPacket(i));
                        MC.getConnection().getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                    }
                    last = i;
                    break;
                }
            }
        }
    }

    public int getTotem(){
        int i =0;
        if(Minecraft.getInstance().player==null){
            return i;
        }
        for(ItemStack item:Minecraft.getInstance().player.getInventory().items){
            if(item.getItem().equals(Items.TOTEM_OF_UNDYING)){
                i++;
            }
        }
        if(Minecraft.getInstance().player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING)){
            i++;
        }
        return i;
    }

    @Override
    public String getName() {
        int t = getTotem();
        return t==0?super.getName():super.getName()+" ["+t+"]";
    }
}
