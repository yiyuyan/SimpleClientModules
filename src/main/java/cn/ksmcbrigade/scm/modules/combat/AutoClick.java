package cn.ksmcbrigade.scm.modules.combat;

import cn.ksmcbrigade.scb.module.Config;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.JNAUtils;
import com.google.gson.JsonObject;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class AutoClick extends Module {

    /*
        0: Left Click
        1: Right Click
        3: Medium Click       */
    public int mode = 0;

    /*
    true: Click
    false: Down           */
    public boolean click = true;

    public int sleepZ;
    public int sleep = 7;

    public AutoClick() throws IOException {
        super(AutoClick.class.getSimpleName(),false, KeyEvent.VK_F12,new Config(new File(AutoClick.class.getSimpleName()),get()),ModuleType.COMBAT);
    }

    public static JsonObject get(){
        JsonObject object = new JsonObject();
        object.addProperty("mode",0);
        object.addProperty("leftClick",true);
        object.addProperty("sleep",7);
        return object;
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        getConfig().reload();
        Random rand = new Random();
        this.mode = Objects.requireNonNull(getConfig().get("mode")).getAsInt();
        this.click = Objects.requireNonNull(getConfig().get("lefTClick")).getAsBoolean();
        int r = rand.nextBoolean()?(Objects.requireNonNull(getConfig().get("sleep")).getAsInt()+rand.nextInt(4)):(Objects.requireNonNull(getConfig().get("sleep")).getAsInt()-rand.nextInt(4));
        this.sleep = r;
        this.sleepZ = r;
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) throws Exception{
        if(player!=null && MC.gameMode!=null){
            if(sleep!=0){
                sleep--;
                return;
            }
            switch (mode){
                case 0:
                    if (click) {
                        if(JNAUtils.JNAInstance.INSTANCE!=null){
                            WinDef.POINT pos = new WinDef.POINT();
                            JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(2,pos.x,pos.y,0,0);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(4,pos.x,pos.y,0,0);
                        }
                    } else {
                        KeyMapping.set(MC.options.keyAttack.getKey(),true);
                        MC.options.keyAttack.setDown(true);
                    }
                    break;
                case 1:
                    if (click) {
                        if(JNAUtils.JNAInstance.INSTANCE!=null){
                            WinDef.POINT pos = new WinDef.POINT();
                            JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(8,pos.x,pos.y,0,0);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(10,pos.x,pos.y,0,0);
                        }
                    } else {
                        KeyMapping.set(MC.options.keyUse.getKey(),true);
                        MC.options.keyUse.setDown(true);
                    }
                    break;
                case 2:
                    if (click) {
                        if(JNAUtils.JNAInstance.INSTANCE!=null){
                            WinDef.POINT pos = new WinDef.POINT();
                            JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(32,pos.x,pos.y,0,0);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(64,pos.x,pos.y,0,0);
                        }
                    } else {
                        KeyMapping.set(MC.options.keyPickItem.getKey(),true);
                        MC.options.keyPickItem.setDown(true);
                    }
                    break;
                default:
                    if (click) {
                        if(JNAUtils.JNAInstance.INSTANCE!=null){
                            WinDef.POINT pos = new WinDef.POINT();
                            JNAUtils.JNAInstance.INSTANCE.GetCursorPos(pos);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(2,pos.x,pos.y,2,0);
                            JNAUtils.JNAInstance.INSTANCE.mouse_event(4,pos.x,pos.y,2,0);
                        }
                    } else {
                        KeyMapping.set(MC.options.keyAttack.getKey(),true);
                        MC.options.keyAttack.setDown(true);
                    }
            }
            sleep = sleepZ;
        }
    }

    @Override
    public void disabled(Minecraft MC) {
        KeyMapping.releaseAll();
    }
}
