package cn.ksmcbrigade.scm.modules.movement;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.block.BlockShapeEvent;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import static cn.ksmcbrigade.scm.modules.movement.FluidJump.shouldBeSolid;

public class WaterJump extends Module {

    public int tick = 10;

    public WaterJump() {
        super(WaterJump.class.getSimpleName(),ModuleType.MOVEMENT);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        ModuleUtils.set("FluidJump",false);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(MC.options.keyShift.isDown()) return;
        if(player==null) return;
        if(player.isInWater()){
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
        Fluid fluid = event.block.getFluidState().getType();
        if((fluid.equals(Fluids.WATER) || (fluid.equals(Fluids.FLOWING_WATER)))){
            event.value = Shapes.block();
        }
    }
}
