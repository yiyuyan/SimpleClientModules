package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.misc.AutoSteal;
import cn.ksmcbrigade.scm.modules.misc.AutoStore;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerScreen.class)
public abstract class ChestMixin extends AbstractContainerScreen<ChestMenu> implements MenuAccess<ChestMenu> {
    @Shadow @Final private int containerRows;

    private int mode;
    private boolean d = true;

    public ChestMixin(ChestMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(Button
                .builder(Component.literal("AutoSteal"), b -> steal(ModuleUtils.get(AutoSteal.class.getSimpleName()).getConfig().get("wait").getAsInt()))
                .bounds(leftPos + imageWidth - 108, topPos-2-12, 50, 12).build());

        this.addRenderableWidget(Button
                .builder(Component.literal("AutoStore"), b -> store(ModuleUtils.get(AutoStore.class.getSimpleName()).getConfig().get("wait").getAsInt()))
                .bounds(leftPos + imageWidth - 56, topPos-2-12, 50, 12).build());


        if(ModuleUtils.enabled(AutoSteal.class.getSimpleName())){
            steal(ModuleUtils.get(AutoSteal.class.getSimpleName()).getConfig().get("wait").getAsInt());
        }

        if(ModuleUtils.enabled(AutoStore.class.getSimpleName())){
            store(ModuleUtils.get(AutoStore.class.getSimpleName()).getConfig().get("wait").getAsInt());
        }
    }

    private void steal(int wait)
    {
        runInThread(() -> shiftClickSlots(0, containerRows * 9, 1,wait));
    }

    private void store(int wait)
    {
        runInThread(() -> shiftClickSlots(containerRows * 9, containerRows * 9 + 44, 2,wait));
    }

    private void runInThread(Runnable r)
    {
        new Thread(() -> {
            try
            {
                r.run();

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    private void shiftClickSlots(int from, int to, int mode,int wait)
    {
        this.mode = mode;

        boolean f = false;

        for(int i = from; i < to; i++)
        {
            Slot slot = getMenu().slots.get(i);
            if(slot.getItem().isEmpty())
                continue;

            if(!f){
                if(this.mode != mode || !d)
                    break;

                this.slotClicked(slot,slot.index,0,ClickType.QUICK_MOVE);
                f = true;
            }

            waitForDelay(wait);
            //minecraft.screen == null
            if(this.mode != mode || !d)
                break;

            this.slotClicked(slot,slot.index,0,ClickType.QUICK_MOVE);
        }
    }

    @Override
    public void onClose() {
        d = false;
        super.onClose();
    }

    private void waitForDelay(int wait)
    {
        try
        {
            Thread.sleep(wait);

        }catch(InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
