package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.render.DeathPosDisplay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    @Shadow @Final private List<Button> exitButtons;

    protected DeathScreenMixin(Component component) {
        super(component);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    target = "net/minecraft/client/gui/screens/Screen.render (Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
            )
    )
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci){
        if(!ModuleUtils.enabled(DeathPosDisplay.class.getSimpleName())) return;
        assert this.minecraft != null;
        assert this.minecraft.player != null;
        String xyz = "[X: " + minecraft.player.blockPosition().getX() + " Y: " + minecraft.player.blockPosition().getY() + " Z: " + minecraft.player.blockPosition().getZ() + "]";
        guiGraphics.drawCenteredString(this.font, xyz, this.width / 2, this.height / 4 + 145, 16777215);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "FIELD",
                    shift = At.Shift.BEFORE,
                    target = "net/minecraft/client/gui/screens/DeathScreen.exitButtons : Ljava/util/List;",
                    ordinal = 2
            )
    )
    protected void init(CallbackInfo ci){
        if(!ModuleUtils.enabled(DeathPosDisplay.class.getSimpleName())) return;
        if(this.minecraft==null){
            return;
        }
        if(this.minecraft.player==null){
            return;
        }
        String xyz =  this.minecraft.player.blockPosition().getX() + " " + minecraft.player.blockPosition().getY() + " " + minecraft.player.blockPosition().getZ();
        this.exitButtons.add(
                this.addRenderableWidget(
                        new Button.Builder(
                                Component.literal("CopyPos"),
                                button -> this.minecraft.keyboardHandler.setClipboard(xyz)
                        ).bounds(
                                this.width / 2 - 100, this.height / 4 + 120, 200, 20
                        ).build()
                )
        );
    }
}
