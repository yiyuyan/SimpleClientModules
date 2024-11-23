package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.network.PacketEvent;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class AutoArmor extends Module {

    private int timer;

    public AutoArmor() throws IOException {
        super(AutoArmor.class.getSimpleName(),false,-1,new Config(new File(AutoArmor.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("wait",2D);
        return object;
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player1) throws Exception{
        if(timer > 0)
        {
            timer--;
            return;
        }

        if(MC.screen instanceof AbstractContainerScreen<?>
                && !(MC.screen instanceof InventoryScreen))
            return;

        LocalPlayer player = Minecraft.getInstance().player;
        Inventory inventory = player.getInventory();

        if((player.input.forwardImpulse != 0
                || player.input.leftImpulse != 0))
            return;

        int[] bestArmorSlots = new int[4];
        int[] bestArmorValues = new int[4];

        for(int type = 0; type < 4; type++)
        {
            bestArmorSlots[type] = -1;

            ItemStack stack = inventory.getArmor(type);
            if(!(stack.getItem() instanceof ArmorItem item)
                    || item instanceof AnimalArmorItem)
                continue;

            bestArmorValues[type] = getArmorValue(item, stack);
        }

        for(int slot = 0; slot < 36; slot++)
        {
            ItemStack stack = inventory.getItem(slot);

            if(!(stack.getItem() instanceof ArmorItem item)
                    || item instanceof AnimalArmorItem)
                continue;

            int armorType = item.getType().ordinal();
            int armorValue = getArmorValue(item, stack);

            if(armorValue > bestArmorValues[armorType])
            {
                bestArmorSlots[armorType] = slot;
                bestArmorValues[armorType] = armorValue;
            }
        }

        ArrayList<Integer> types = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(types);
        for(int type : types)
        {
            int slot = bestArmorSlots[type];
            if(slot == -1)
                continue;

            ItemStack oldArmor = inventory.getArmor(type);
            if(!oldArmor.isEmpty() && inventory.getFreeSlot() == -1)
                continue;

            if(slot < 9)
                slot += 36;

            if(!oldArmor.isEmpty()) {
                assert Minecraft.getInstance().gameMode != null;
                Minecraft.getInstance().gameMode.handleInventoryMouseClick(0,8 - type,0, ClickType.QUICK_MOVE,player);
            }
            assert Minecraft.getInstance().gameMode != null;
            Minecraft.getInstance().gameMode.handleInventoryMouseClick(0,slot,0, ClickType.QUICK_MOVE,player);

            break;
        }
    }

    @Override
    public void packetEvent(Minecraft MC, PacketEvent event) {
        if(event.packet instanceof ServerboundContainerClickPacket)
            timer = (int)getConfig().get("wait").getAsDouble();
    }

    private int getArmorValue(ArmorItem item, ItemStack stack)
    {
        int armorPoints = item.getDefense();
        int prtPoints;
        int armorToughness = (int)item.getToughness();
        int armorType = item.getMaterial().value().getDefense(ArmorItem.Type.LEGGINGS);

        RegistryAccess drm =
                Minecraft.getInstance().level.registryAccess();
        Registry<Enchantment> registry = drm.registry(Registries.ENCHANTMENT).get();

        Optional<Holder.Reference<Enchantment>> protection =
                registry.getHolder(Enchantments.PROTECTION);

        prtPoints = protection
                .map(entry -> EnchantmentHelper.getItemEnchantmentLevel(entry, stack))
                .orElse(0);

        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }
}
