package cn.ksmcbrigade.scm.modules.overlay;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.module.events.misc.CheckHasEffectEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;

public class NoDarknessOverlay extends Module {

    public NoDarknessOverlay() {
        super(NoDarknessOverlay.class.getSimpleName(),ModuleType.OVERLAY);
    }

    @Override
    public void hasEffect(Minecraft mc, CheckHasEffectEvent event) throws Exception {
        if(event.effect.value().equals(MobEffects.DARKNESS)){
            event.has = false;
        }
    }
}
