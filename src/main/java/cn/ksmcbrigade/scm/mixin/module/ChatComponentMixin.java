package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.misc.AntiSpam;
import cn.ksmcbrigade.scm.modules.render.ChatUp;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {

    @Shadow @Final private List<GuiMessage> allMessages;

    @Shadow protected abstract void refreshTrimmedMessages();

    @Unique
    private ArrayList<Component> messages = new ArrayList<>();

    @Unique
    public int simpleClientModules$get(){
        Minecraft MC = Minecraft.getInstance();
        if(MC.player==null || MC.player.isCreative() || MC.player.isSpectator()) return 0;
        return ((MC.player.getAbsorptionAmount()>0)?((MC.player.getArmorValue()>0?10:0)+10):(MC.player.getArmorValue()>0?10:0));
    }

    @ModifyArg(method = "render",index = 1,at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",ordinal = 0))
    public float getY(float y){
        if(ModuleUtils.enabled(ChatUp.class.getSimpleName())) return y - simpleClientModules$get();
        return y;
    }

    @ModifyConstant(method = "screenToChatY",constant = @Constant(doubleValue = 40.0D))
    public double chatY(double constant){
        if(ModuleUtils.enabled(ChatUp.class.getSimpleName())) return constant + simpleClientModules$get();
        return constant;
    }

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V",at = @At("HEAD"),cancellable = true)
    public void add(Component p_241484_, MessageSignature p_241323_, GuiMessageTag p_241297_, CallbackInfo ci){
        messages.add(p_241484_);
        System.out.println(p_241484_.getString());
    }

    @ModifyArg(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GuiMessage;<init>(ILnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V"), index = 1)
    public Component get(Component value){
        if(ModuleUtils.enabled(AntiSpam.class.getSimpleName())){
            int count = simpleClientModules$count(value);
            if(count>1){
                for (int i = 0; i < (count-1); i++) {
                    Component component = i==0?value:MutableComponent.create(value.getContents()).append(CommonComponents.SPACE).append("[").append(String.valueOf(i+1)).append("]");
                    GuiMessage message = simpleClientModules$getMessage(component);
                    if(message!=null){
                        this.simpleClientModules$delete(message);
                    }
                }

                return MutableComponent.create(value.getContents()).append(CommonComponents.SPACE).append("[").append(String.valueOf(count)).append("]");
            }
        }
        return value;
    }

    @Inject(method = "clearMessages",at = @At("HEAD"))
    public void clear(boolean p_93796_, CallbackInfo ci){
        messages.clear();
    }

    @Unique
    private int simpleClientModules$count(Component context){
        int i=0;
        for (Component message : messages) {
            if(message.equals(context)){
                i++;
            }
        }
        return i;
    }

    @Unique
    private GuiMessage simpleClientModules$getMessage(Component value){
        for (GuiMessage allMessage : this.allMessages) {
            if(allMessage.content().equals(value)){
                return allMessage;
            }
        }
        return null;
    }

    @Unique
    private void simpleClientModules$delete(GuiMessage message){
        this.allMessages.remove(message);
        this.refreshTrimmedMessages();
    }
}
