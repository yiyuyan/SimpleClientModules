package cn.ksmcbrigade.scm.modules.block;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scm.modules.combat.AutoSword;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class AutoTool extends Module {
    public AutoTool() {
        super(AutoTool.class.getSimpleName(),ModuleType.BLOCK);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(player==null) return;
        if(MC.level==null) return;
        HitResult hitResult = MC.hitResult;
        if((hitResult instanceof BlockHitResult blockHitResult)){
            Block block = MC.level.getBlockState(blockHitResult.getBlockPos()).getBlock();
            String reg = BuiltInRegistries.BLOCK.getKey(block).getPath();
            Inventory inventory = player.getInventory();
            int result = -1;
            if(reg.toLowerCase().contains("ore") || reg.toLowerCase().contains("stone") || reg.toLowerCase().contains("granite") || reg.toLowerCase().contains("andesite") || reg.toLowerCase().contains("diorite")){
                result = find(inventory, PickaxeItem.class);
            }
            else if(((block instanceof RotatedPillarBlock) && !(block instanceof HayBlock) && !(block instanceof ChainBlock)) || reg.toLowerCase().contains("wood") || reg.toLowerCase().contains("planks")){
                result = find(inventory, AxeItem.class);
            }
            else if(block instanceof HayBlock || block instanceof FarmBlock){
                result = find(inventory, HoeItem.class);
            }
            else if(block instanceof SnowyDirtBlock || block instanceof DirtPathBlock || block instanceof RootedDirtBlock || reg.toLowerCase().contains("dirt")){
                result = find(inventory, ShovelItem.class);
            }
            else if(block instanceof WebBlock){
                result = find(inventory, SwordItem.class);
            }
            if(result!=-1){
                inventory.selected = result;
            }
        }
    }

    public static int find(Inventory inventory, Class<? extends Item> target){
        int result = -1;
        for (int i = 0; i < 9; i++) {
            Item item = inventory.getItem(i).getItem();
            if(item.getClass().equals(target)){
                if(result==-1 || AutoSword.thanLast(item,inventory.getItem(result).getItem())){
                    result = i;
                }
            }
        }
        return result;
    }
}
