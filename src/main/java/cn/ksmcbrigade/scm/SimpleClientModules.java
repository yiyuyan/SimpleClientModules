package cn.ksmcbrigade.scm;

import cn.ksmcbrigade.scb.config.HUDConfig;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.modules.block.AutoMine;
import cn.ksmcbrigade.scm.modules.block.FastPlace;
import cn.ksmcbrigade.scm.modules.combat.*;
import cn.ksmcbrigade.scm.modules.misc.AutoSwitch;
import cn.ksmcbrigade.scm.modules.movement.*;
import cn.ksmcbrigade.scm.modules.overlay.NoBackground;
import cn.ksmcbrigade.scm.modules.overlay.NoHnadOverlay;
import cn.ksmcbrigade.scm.modules.overlay.NoNetherOverlay;
import cn.ksmcbrigade.scm.modules.render.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SimpleClientModules.MODID)
@EventBusSubscriber(value = Dist.CLIENT,modid = SimpleClientModules.MODID)
public class SimpleClientModules
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "scm";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public SimpleClientModules(IEventBus modEventBus, ModContainer modContainer) throws IOException, IllegalAccessException {

        HUDConfig.init();
        ModuleType.init();

        ModuleUtils.add(new KillAura());
        ModuleUtils.add(new TpAura());
        ModuleUtils.add(new AutoLog());
        ModuleUtils.add(new AutoClick());
        ModuleUtils.add(new AutoTotem());

        ModuleUtils.add(new AutoMine());
        ModuleUtils.add(new FastPlace());

        ModuleUtils.add(new AutoSneak());
        ModuleUtils.add(new AutoSpring());
        ModuleUtils.add(new ClickTp());
        ModuleUtils.add(new CreativeFlight());
        ModuleUtils.add(new NoGravity());
        ModuleUtils.add(new BoatFly());
        ModuleUtils.add(new ElytraFly());
        ModuleUtils.add(new SafeWalk());
        ModuleUtils.add(new EdgeJump());
        ModuleUtils.add(new FluidJump());
        ModuleUtils.add(new WaterJump());
        ModuleUtils.add(new LavaJump());
        ModuleUtils.add(new Step());
        ModuleUtils.add(new Spider());
        ModuleUtils.add(new HighJump());

        ModuleUtils.add(new NoWeather());
        ModuleUtils.add(new FreeCam());
        ModuleUtils.add(new XRay());
        ModuleUtils.add(new HighCameraDistance());
        ModuleUtils.add(new NoCameraClip());
        ModuleUtils.add(new OverWorldXYZ());
        ModuleUtils.add(new XYZ());
        ModuleUtils.add(new Ping());
        ModuleUtils.add(new FPS());
        ModuleUtils.add(new InvNether());
        ModuleUtils.add(new NoNetherOverlay());

        ModuleUtils.add(new NoHnadOverlay());
        ModuleUtils.add(new NoBackground());

        ModuleUtils.add(new AutoSwitch());

        LOGGER.info("SimpleClient modules loaded.");
    }

    @SubscribeEvent
    public static void click(PlayerInteractEvent.RightClickEmpty event){
        if (ModuleUtils.get(ClickTp.class.getSimpleName()).enabled && Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().getConnection().send(new ServerboundMovePlayerPacket.Pos(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity().onGround()));
        }
    }
}
