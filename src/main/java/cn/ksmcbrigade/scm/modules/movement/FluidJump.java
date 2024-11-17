package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.block.BlockShapeEvent;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;

public class FluidJump extends Module {

    public int tick = 10;

    public FluidJump() {
        super(FluidJump.class.getSimpleName(),false, KeyEvent.VK_J,ModuleType.MOVEMENT);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        ModuleUtils.set("WaterJump",false);
        ModuleUtils.set("LavaJump",false);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(MC.options.keyShift.isDown()) return;
        if(player==null) return;
        if(player.isInFluidType()){
            Vec3 vec3 = player.getViewVector(0);
            player.setDeltaMovement(0,0.11,0);
            tick = 0;
            return;
        }
        Vec3 vec3 = player.getViewVector(0);
        if(tick==0){
            player.setDeltaMovement(0,0.3,0);
        }
        else if(tick==1){
            player.setDeltaMovement(0,0,0);
        }
        tick++;
    }

    @Override
    public void blockShape(Minecraft MC, BlockShapeEvent event) throws Exception {
        if(event.block.getFluidState().isEmpty()) return;
        if(!shouldBeSolid(event.block.getFluidState())) return;
        event.value = Shapes.block();
    }

    public static boolean shouldBeSolid(FluidState state)
    {
        Minecraft MC = Minecraft.getInstance();
        return (ModuleUtils.enabled(WaterJump.class.getSimpleName()) || ModuleUtils.enabled(LavaJump.class.getSimpleName()) || ModuleUtils.enabled(FluidJump.class.getSimpleName())) && MC.player != null && MC.player.fallDistance <= 3
                && !MC.options.keyShift.isDown() && !MC.player.isInWater()
                && !MC.player.isInLava();
    }
}
