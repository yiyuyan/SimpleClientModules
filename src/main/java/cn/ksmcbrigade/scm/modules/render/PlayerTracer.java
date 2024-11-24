package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;

public class PlayerTracer extends Module {

    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("blockInvisible",true);
        object.addProperty("blockSleeping",true);
        object.addProperty("blockFlight",false);
        object.addProperty("red", 173.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public PlayerTracer() throws IOException {
        super(PlayerTracer.class.getSimpleName(),false,-1,new Config(new File(PlayerTracer.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void renderLevel(Minecraft mc, RenderLevelStageEvent event) {

        Minecraft MC = Minecraft.getInstance();
        if(Minecraft.getInstance().player==null) return;
        com.mojang.blaze3d.vertex.Tesselator tessellator = Tesselator.getInstance();

        for (AbstractClientPlayer player : MC.level.players()) {

            if(player.getId()==Minecraft.getInstance().player.getId()) continue;
            if(player.isInvisible() && getConfig().get("blockInvisible").getAsBoolean()) continue;
            if((player.isFallFlying() || player.mayFly()) && getConfig().get("blockFlight").getAsBoolean()) continue;
            if((player.isSleeping() && getConfig().get("blockSleeping").getAsBoolean())) continue;

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableDepthTest();

            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            Vec3 from = MC.gameRenderer.getMainCamera().getPosition();
            Vec3 to = player.getPosition(0);

            float x = (float) (to.x - from.x);
            float y = (float) (to.y - from.y);
            float z = (float) (to.z - from.z);

            event.getPoseStack().pushPose();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            bufferBuilder.addVertex((float) MC.player.getViewVector(0).x, (float) MC.player.getViewVector(0).y, (float) MC.player.getViewVector(0).z).setColor(getConfig().get("red").getAsFloat()/255f,getConfig().get("green").getAsFloat()/255f,getConfig().get("blue").getAsFloat()/255f,getConfig().get("opacity").getAsFloat());
            bufferBuilder.addVertex(x,y+player.getBbHeight()/2,z).setColor(getConfig().get("red").getAsFloat()/255f,getConfig().get("green").getAsFloat()/255f,getConfig().get("blue").getAsFloat()/255f,getConfig().get("opacity").getAsFloat());

            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());

            event.getPoseStack().popPose();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }

        //RenderSystem.setShaderColor(getConfig().get("red").getAsFloat()/255f,getConfig().get("green").getAsFloat()/255f,getConfig().get("blue").getAsFloat()/255f,getConfig().get("opacity").getAsFloat());
    }
}
