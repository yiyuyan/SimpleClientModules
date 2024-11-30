package cn.ksmcbrigade.scm.mixin.fix.list;

import cn.ksmcbrigade.scb.config.HUD02Config;
import cn.ksmcbrigade.scb.guis.anotherFeatureList.FeatureList2;
import cn.ksmcbrigade.scb.guis.anotherFeatureList.widgets.FeatureRenderer;
import cn.ksmcbrigade.scb.guis.anotherFeatureList.widgets.SearchBox;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.function.Consumer;

@Mixin(SearchBox.class)
public abstract class FixSearchBox extends EditBox {
    @Shadow @Final public ArrayList<FeatureRenderer> last;

    @Shadow @Final public FeatureList2 instance;

    @Shadow @Final public int heightSize;

    public FixSearchBox(Font p_294485_, int p_294264_, int p_295938_, Component p_294624_) {
        super(p_294485_, p_294264_, p_295938_, p_294624_);
    }

    @Unique
    public void setResponder(@NotNull Consumer<String> p_94152_) {
        super.setResponder((str)->{
            p_94152_.accept(str);
            if(Minecraft.getInstance().options.guiScale().get()<=1) return;
            if(str.isEmpty()){
                for (FeatureRenderer renderer : this.last) {
                    this.instance.removeWidget(renderer);
                }
                this.last.clear();
                return;
            }
            for (FeatureRenderer renderer : this.last) {
                this.instance.removeWidget(renderer);
            }
            this.last.clear();
            ArrayList<Module> modules = ModuleUtils.getAll(false);

            for(Module module:modules) {
                if (module.getName().toLowerCase().contains(str.toLowerCase())) {
                    FeatureRenderer renderer = new FeatureRenderer(instance, this.getX(), this.getY() + this.getHeight() + this.heightSize + this.last.size() * 12, 50, 12, -15693574, -15667718, HUD02Config.MODULE_ENABLED_COLORd.get(), module, module.type.group);
                    renderer.configRenderer.setY(renderer.configRenderer.getY()-2);
                    this.last.add(renderer);
                }
            }

            for (FeatureRenderer renderer : this.last) {
                this.instance.addWidget(renderer);
            }
        });
    }
}
