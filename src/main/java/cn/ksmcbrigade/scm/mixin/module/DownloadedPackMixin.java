package cn.ksmcbrigade.scm.mixin.module;

import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.misc.ASRP;
import com.mojang.util.UndashedUuid;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.client.User;
import net.minecraft.client.resources.server.DownloadedPackSource;
import net.minecraft.client.resources.server.PackDownloader;
import net.minecraft.server.packs.DownloadQueue;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@Mixin(DownloadedPackSource.class)
public class DownloadedPackMixin {
    @Inject(method = "createDownloader",at = @At("HEAD"), cancellable = true)
    public void down(DownloadQueue p_314570_, Executor p_314421_, User p_314576_, Proxy p_314551_, CallbackInfoReturnable<PackDownloader> cir){
        if(ModuleUtils.enabled(ASRP.class.getSimpleName())){
            cir.setReturnValue(new PackDownloader() {

                private Map<String, String> createDownloadHeaders() {
                    WorldVersion worldversion = SharedConstants.getCurrentVersion();
                    return Map.of(
                            "X-Minecraft-Username",
                            p_314576_.getName(),
                            "X-Minecraft-UUID",
                            UndashedUuid.toString(p_314576_.getProfileId()),
                            "X-Minecraft-Version",
                            worldversion.getName(),
                            "X-Minecraft-Version-ID",
                            worldversion.getId(),
                            "X-Minecraft-Pack-Format",
                            String.valueOf(worldversion.getPackVersion(PackType.CLIENT_RESOURCES)),
                            "User-Agent",
                            "Minecraft Java/" + worldversion.getName()
                    );
                }

                @Override
                public void download(@NotNull Map<UUID, DownloadQueue.DownloadRequest> p_314430_, @NotNull Consumer<DownloadQueue.BatchResult> p_314486_) {
                    //do nothing
                }
            });
            cir.cancel();
        }
    }
}
