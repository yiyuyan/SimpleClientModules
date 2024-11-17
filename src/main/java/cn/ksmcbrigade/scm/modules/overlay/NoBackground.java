package cn.ksmcbrigade.scm.modules.overlay;

import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class NoBackground extends Module {

    public NoBackground() {
        super(NoBackground.class.getSimpleName(),ModuleType.OVERLAY);
    }

    public static boolean renderCancel(){
        Minecraft MC = Minecraft.getInstance();
        Screen screen = MC.screen;
        if(MC.level==null) return false;
        return screen instanceof AbstractContainerScreen<?>;
    }
}
