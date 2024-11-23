package cn.ksmcbrigade.scm.mixin.fix.list;

import cn.ksmcbrigade.scb.guis.anotherFeatureList.FeatureList2;
import cn.ksmcbrigade.scb.guis.anotherFeatureList.widgets.TypeList;
import cn.ksmcbrigade.scb.guis.group.Group;
import cn.ksmcbrigade.scm.utils.TypeListAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TypeList.class)
public class TypeListAccessMixin implements TypeListAccess {
    private FeatureList2 instance;

    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(FeatureList2 instance, String title, int x, int y, int width, int height, int color, int cur_color, int enabled_color, Group group, CallbackInfo ci){
        this.instance = instance;
    }


    @Override
    public FeatureList2 getInstance() {
        return this.instance;
    }
}
