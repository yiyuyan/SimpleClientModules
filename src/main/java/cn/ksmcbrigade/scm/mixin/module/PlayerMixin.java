package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.movement.HighJump;
import com.google.gson.JsonElement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }


    @Unique
    public float getJumpBoostPower() {
        float r = super.getJumpBoostPower();
        if(ModuleUtils.enabled(HighJump.class.getSimpleName())){
            if(ModuleUtils.get(HighJump.class.getSimpleName()).getConfig()==null){
                r+=0.1F*6.0F;
            }
            else{
                JsonElement config = ModuleUtils.get(HighJump.class.getSimpleName()).getConfig().get("block");
                if(config==null){
                    r+=0.1F*6.0F;
                }
                else{
                    r+= config.getAsFloat()*0.1F;
                }
            }
        }
        return r;
    }
}
