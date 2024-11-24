package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.misc.ASRP;
import net.minecraft.server.packs.DownloadQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(DownloadQueue.class)
public class DownloadPackMixin {
    @Inject(method = "runDownload",at = @At("HEAD"), cancellable = true)
    public void down(DownloadQueue.BatchConfig p_314482_, Map<UUID, DownloadQueue.DownloadRequest> p_314452_, CallbackInfoReturnable<DownloadQueue.BatchResult> cir){
        if(ModuleUtils.enabled(ASRP.class.getSimpleName())){
            cir.setReturnValue(new DownloadQueue.BatchResult());
            cir.cancel();
        }
    }
}
