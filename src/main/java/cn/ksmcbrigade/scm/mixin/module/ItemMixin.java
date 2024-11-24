package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.combat.AutoEat;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsingItem",at = @At("TAIL"))
    public void finish(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_, CallbackInfoReturnable<ItemStack> cir){
        if(p_41411_.equals(Minecraft.getInstance().player)){
            Module module = ModuleUtils.get(AutoEat.class.getSimpleName());
            if(module!=null && module.enabled){
                ((AutoEat)module).d = false;
            }
        }
    }
}
