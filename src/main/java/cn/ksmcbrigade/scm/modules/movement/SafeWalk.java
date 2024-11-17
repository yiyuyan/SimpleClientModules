package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class SafeWalk extends Module {

    public boolean sneak = false;

    public SafeWalk() {
        super(SafeWalk.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        ModuleUtils.set("EdgeJump",false);
        this.sneak = false;
    }

    @Override
    public void disabled(Minecraft MC) {
        if (this.sneak) {
            set(false);
        }
    }

    public void clip(boolean clip,Minecraft MC, @Nullable Player player){
        if(player==null) return;
        if(MC.level==null) return;
        if(!enabled || !player.onGround()){
            if(sneak) set(false);
            return;
        }
        AABB box = player.getBoundingBox();
        AABB adjustedBox = box.expandTowards(0, -player.maxUpStep(), 0).inflate(-0.01, 0, -0.01);

        if(MC.level.noCollision(player, adjustedBox)) clip = true;

        set(clip);
    }

    public void set(boolean sneak){
        Minecraft MC = Minecraft.getInstance();
        if(sneak){
            MC.options.keyShift.setDown(true);
        }
        else{
            MC.options.keyShift.setDown(false);
        }
        this.sneak = sneak;
    }
}
