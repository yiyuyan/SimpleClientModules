package cn.ksmcbrigade.scm.mixin.fix.list;

import cn.ksmcbrigade.scb.guis.anotherFeatureList.FeatureList2;
import cn.ksmcbrigade.scb.guis.anotherFeatureList.widgets.FeatureConfigRenderer;
import cn.ksmcbrigade.scb.guis.anotherFeatureList.widgets.FeatureRenderer;
import cn.ksmcbrigade.scb.guis.anotherFeatureList.widgets.TypeList;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scm.utils.TypeListAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;

@Mixin(FeatureList2.class)
public abstract class FixModuleHUD extends Screen {
    @Shadow @Final private ArrayList<TypeList> lists;

    protected FixModuleHUD(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init",at = @At("TAIL"))
    public void init(CallbackInfo ci) throws NoSuchFieldException, IllegalAccessException {
        if(Minecraft.getInstance().options.guiScale().get()>1){
            for (TypeList list : this.lists) {
                ArrayList<FeatureRenderer> featureRenderers = new ArrayList<>();
                for (int i = 0; i < list.renderers.size(); i++) {
                    Module module = list.group.featureList.renderer.featureRenderers.get(i).feature;
                    FeatureRenderer last = list.renderers.get(i);
                    FeatureConfigRenderer configRenderer = last.configRenderer;
                    this.removeWidget(configRenderer);
                    this.removeWidget(last);
                    FeatureRenderer renderer = this.addWidget(new FeatureRenderer(((TypeListAccess)list).getInstance(), list.getX(), list.getY() + list.getHeight() + (list.getWidth()/2) / 2 * i, list.getWidth(), list.getWidth() / 2 / 2, list.color, list.cur_color, list.enabled_color, module, list.group));
                    renderer.configRenderer.setY(renderer.configRenderer.getY()-2);
                    featureRenderers.add(renderer);
                }
                Field field = list.getClass().getDeclaredField("renderers");
                field.setAccessible(true);
                field.set(list,featureRenderers);
            }
        }
    }
}
