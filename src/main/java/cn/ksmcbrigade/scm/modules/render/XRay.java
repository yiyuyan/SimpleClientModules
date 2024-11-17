package cn.ksmcbrigade.scm.modules.render;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.render.RenderBlockEvent;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class XRay extends Module {

    public Vec3 pos;
    public float speed = 0.05F;

    public XRay() throws IllegalAccessException, IOException {
        super(XRay.class.getSimpleName(),false, KeyEvent.VK_X,new Config(new File(XRay.class.getSimpleName()),get()),ModuleType.RENDER);
    }

    public static JsonObject get() throws IllegalAccessException {
        JsonObject object = new JsonObject();
        StringBuilder builder = new StringBuilder();
        for (Field declaredField : Blocks.class.getDeclaredFields()) {
            if(declaredField.getType().equals(Block.class)){
                declaredField.setAccessible(true);
                Block block = (Block) declaredField.get(null);
                if(block.getName().getString().toLowerCase().contains("ore")){
                    builder.append(builder.isEmpty()?"":",").append(BuiltInRegistries.BLOCK.getKey(block));
                }
            }
        }
        object.addProperty("blocks",builder.toString());
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws IOException {
        getConfig().reload();
        MC.reloadResourcePacks();
    }

    @Override
    public void disabled(Minecraft MC) {
        MC.reloadResourcePacks();
    }

    @Override
    public void renderBlockEvent(Minecraft MC, RenderBlockEvent event) throws Exception {
        event.render = getConfig().get("blocks").getAsString().contains(BuiltInRegistries.BLOCK.getKey(event.block.getBlock()).toString());
    }
}
