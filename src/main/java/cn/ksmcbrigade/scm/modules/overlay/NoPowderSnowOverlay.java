package cn.ksmcbrigade.scm.modules.overlay;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.render.RenderOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class NoPowderSnowOverlay extends Module {

    public static final ResourceLocation POWDER_SNOW_OUTLINE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/powder_snow_outline.png");

    public NoPowderSnowOverlay() {
        super(NoPowderSnowOverlay.class.getSimpleName(),ModuleType.OVERLAY);
    }

    @Override
    public void renderTexOverlayEvent(Minecraft MC, RenderOverlayEvent event) {
        if(event.resourceLocation.equals(NoPowderSnowOverlay.POWDER_SNOW_OUTLINE_LOCATION)){
            event.setCanceled(true);
        }
    }
}
