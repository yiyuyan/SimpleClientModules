package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.RenderUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.io.File;
import java.io.IOException;

public class ItemESP extends Module {

    public ItemESP() throws IOException {
        super(ItemESP.class.getSimpleName(),false,-1,new Config(new File(ItemESP.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    public void enabled(Minecraft MC) throws Exception {
        this.getConfig().reload();
    }

    public static JsonObject get() {
        JsonObject object = new JsonObject();
        object.addProperty("red", 173.0F);
        object.addProperty("green", 216.0F);
        object.addProperty("blue", 230.0F);
        object.addProperty("opacity", 1.0F);
        return object;
    }

    public void renderLevel(Minecraft mc, RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            if (Minecraft.getInstance().player != null) {
                for (ItemEntity player : Minecraft.getInstance().level.getEntitiesOfClass(ItemEntity.class, (new AABB(Minecraft.getInstance().player.getPosition(0.0F), Minecraft.getInstance().player.getPosition(0.0F))).inflate((double) (Minecraft.getInstance().options.getEffectiveRenderDistance() * 16)))) {
                    RenderUtils.renderPlayer(event.getPoseStack(), event.getModelViewMatrix(), event.getProjectionMatrix(), player.getPosition(0.0F).add(-0.25, 0.0, -0.25), player, this.getConfig().get("red").getAsFloat() / 250.0F, this.getConfig().get("green").getAsFloat() / 250.0F, this.getConfig().get("blue").getAsFloat() / 250.0F, this.getConfig().get("opacity").getAsFloat());
                }
            }
        }
    }
}
