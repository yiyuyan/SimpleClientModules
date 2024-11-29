package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.RenderUtils;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerPosesDisplay extends Module {

    private final ArrayList<Vec3> pos = new ArrayList<>();

    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("blockSpectator",true);
        object.addProperty("red", 173.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public PlayerPosesDisplay() throws IOException {
        super(PlayerPosesDisplay.class.getSimpleName(),false,-1,new Config(new File(PlayerPosesDisplay.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        pos.clear();
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception {
        if(player==null) return;
        if(player.isSpectator() && getConfig().get("blockSpectator").getAsBoolean()) return;
        Vec3 get = player.getPosition(0.05F);
        if(!pos.contains(get)){
            pos.add(get);
        }
    }

    @Override
    public void renderLevel(Minecraft mc, RenderLevelStageEvent event) {

        Minecraft MC = Minecraft.getInstance();
        if(pos.isEmpty()) return;
        if(Minecraft.getInstance().player==null) return;
        Tesselator tessellator = Tesselator.getInstance();

        PoseStack pose = event.getPoseStack();
        Vec3 cameraPos = MC.gameRenderer.getMainCamera().getPosition();

        pose.pushPose();
        pose.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);

        BufferBuilder builder = tessellator.begin(VertexFormat.Mode.DEBUG_LINE_STRIP,DefaultVertexFormat.POSITION_COLOR);
        try{
            for (Vec3 po : pos) {
                float x = (float) (po.x - cameraPos.x);
                float y = (float) (po.y - cameraPos.y);
                float z = (float) (po.z - cameraPos.z);
                builder.addVertex(x,y+0.015F,z).setColor(getConfig().get("red").getAsFloat()/250f,getConfig().get("green").getAsFloat()/250F,getConfig().get("blue").getAsFloat()/250F,getConfig().get("opacity").getAsFloat());
            }
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            BufferUploader.drawWithShader(builder.buildOrThrow());
        }
        catch(Exception e){
            //nothing
        }
        pose.popPose();
    }
}
