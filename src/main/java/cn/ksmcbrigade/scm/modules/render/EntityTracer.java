package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.io.File;
import java.io.IOException;

public class EntityTracer extends Module {

    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("blockInvisible",true);
        object.addProperty("blockSleeping",true);
        object.addProperty("blockFlight",false);
        object.addProperty("blockAnimals",true);
        object.addProperty("blockMonsters",false);
        object.addProperty("blockPlayers",false);
        object.addProperty("livingOnly",false);
        object.addProperty("red", 173.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public EntityTracer() throws IOException {
        super(EntityTracer.class.getSimpleName(),false,-1,new Config(new File(EntityTracer.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void renderLevel(Minecraft mc, RenderLevelStageEvent event) {

        Minecraft MC = Minecraft.getInstance();
        if(Minecraft.getInstance().player==null) return;

        for (Entity player : MC.level.getEntities().getAll()) {

            if(!(player instanceof LivingEntity) && getConfig().get("livingOnly").getAsBoolean()) continue;
            if(player.getId()==Minecraft.getInstance().player.getId()) continue;
            if(player.isInvisible() && getConfig().get("blockInvisible").getAsBoolean()) continue;
            if(player instanceof LivingEntity living){
                if((living.isFallFlying()) && getConfig().get("blockFlight").getAsBoolean()) continue;
                if((living.isSleeping() && getConfig().get("blockSleeping").getAsBoolean())) continue;
            }
            if(player instanceof Player player1){
                if(getConfig().get("blockPlayers").getAsBoolean()) continue;
                if(player1.isFallFlying() || player1.mayFly() && getConfig().get("blockFlight").getAsBoolean()) continue;
            }
            if((player instanceof Monster) && getConfig().get("blockMonsters").getAsBoolean()) continue;
            if((player instanceof Animal) && getConfig().get("blockAnimals").getAsBoolean()) continue;

            Vec3 from = MC.player.getPosition(0);
            Vec3 fromO = MC.player.getViewVector(0);
            Vec3 to = player.getPosition(0);

            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

            float x = (float) (to.x - from.x);
            float y = (float) (to.y - from.y);
            float z = (float) (to.z - from.z);

            bufferBuilder.addVertex((float) fromO.x, (float) fromO.y, (float) fromO.z).setColor(getConfig().get("red").getAsFloat()/255f,getConfig().get("green").getAsFloat()/255f,getConfig().get("blue").getAsFloat()/255f,getConfig().get("opacity").getAsFloat());
            bufferBuilder.addVertex(x,y,z).setColor(getConfig().get("red").getAsFloat()/255f,getConfig().get("green").getAsFloat()/255f,getConfig().get("blue").getAsFloat()/255f,getConfig().get("opacity").getAsFloat());

            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());

            RenderSystem.disableDepthTest();
        }
    }
}
