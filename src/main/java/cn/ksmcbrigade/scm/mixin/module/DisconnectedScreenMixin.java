package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.config.TempConfig;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.combat.AutoReconnect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends Screen {

    @Shadow
    @Final
    private LinearLayout layout;

    @Shadow @Final private Screen parent;
    @Unique
    public Button button;

    @Unique
    public Button recButton;

    @Unique
    public int timer;

    protected DisconnectedScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init",at = @At("TAIL"))
    public void init(CallbackInfo ci){

        this.button = this.layout.addChild(Button.builder(Component.literal("Reconnect"),b -> this.rec()).width(200).build());
        this.recButton = this.layout.addChild(Button.builder(Component.literal("AutoReconnect"),b -> {
            try {
                press();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).width(200).build());

        this.layout.arrangeElements();

        addRenderableWidget(this.button);
        addRenderableWidget(this.recButton);

        if(ModuleUtils.enabled(AutoReconnect.class.getSimpleName())){
            timer = ModuleUtils.get(AutoReconnect.class.getSimpleName()).getConfig().get("waitSecond").getAsInt()*20;
        }
    }

    @Unique
    public void press() throws Exception {

        ModuleUtils.set(AutoReconnect.class.getSimpleName(),!ModuleUtils.enabled(AutoReconnect.class.getSimpleName()));

        if(ModuleUtils.enabled(AutoReconnect.class.getSimpleName())){
            timer = ModuleUtils.get(AutoReconnect.class.getSimpleName()).getConfig().get("waitSecond").getAsInt()*20;
        }
    }

    @Unique
    public void rec(){
        if(TempConfig.lastIp==null) return;
        ConnectScreen.startConnecting(this.parent, Minecraft.getInstance(), ServerAddress.parseString(TempConfig.lastIp), new ServerData("",TempConfig.lastIp, ServerData.Type.OTHER),false,null);
    }

    @Unique
    public void tick(){
        if(recButton==null) return;
        if(!ModuleUtils.enabled(AutoReconnect.class.getSimpleName())){
            recButton.setMessage(Component.literal("AutoReconnect"));
            return;
        }

        recButton.setMessage(Component.literal("AutoReconnect").append(Component.literal(" ("+(int)Math.ceil(timer/20.0D)+")")));

        if(timer>0){
            timer--;
            return;
        }

        rec();
    }
}
