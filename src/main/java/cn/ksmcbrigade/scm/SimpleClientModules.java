package cn.ksmcbrigade.scm;

import cn.ksmcbrigade.scb.config.HUDConfig;
import cn.ksmcbrigade.scb.module.ModuleType;
import cn.ksmcbrigade.scb.uitls.CommandUtils;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import cn.ksmcbrigade.scm.commands.BindCommand;
import cn.ksmcbrigade.scm.commands.listModulesCommand;
import cn.ksmcbrigade.scm.commands.setCommand;
import cn.ksmcbrigade.scm.modules.block.AirPlace;
import cn.ksmcbrigade.scm.modules.block.AutoMine;
import cn.ksmcbrigade.scm.modules.block.FastMine;
import cn.ksmcbrigade.scm.modules.block.FastPlace;
import cn.ksmcbrigade.scm.modules.combat.*;
import cn.ksmcbrigade.scm.modules.misc.*;
import cn.ksmcbrigade.scm.modules.movement.*;
import cn.ksmcbrigade.scm.modules.overlay.*;
import cn.ksmcbrigade.scm.modules.render.*;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SimpleClientModules.MODID)
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
        ModuleUtils.add(new AutoReconnect());
        ModuleUtils.add(new AntiProjectiles());
        ModuleUtils.add(new AntiKnockback());
        ModuleUtils.add(new Reach());
        ModuleUtils.add(new AutoArmor());
        ModuleUtils.add(new AutoSword());
        ModuleUtils.add(new AttackAutoSword());
        ModuleUtils.add(new AutoEat());
        ModuleUtils.add(new Criticals());

        ModuleUtils.add(new AutoMine());
        ModuleUtils.add(new FastPlace());
        ModuleUtils.add(new FastMine());
        ModuleUtils.add(new AirPlace());

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
        ModuleUtils.add(new SnowShoe());
        ModuleUtils.add(new Fish());
        ModuleUtils.add(new FastLadder());
        ModuleUtils.add(new NoWeb());
        ModuleUtils.add(new NoSlowDown());
        ModuleUtils.add(new AntiHungry());
        ModuleUtils.add(new AntiCactus());
        ModuleUtils.add(new AntiEntityPush());
        ModuleUtils.add(new Blink());
        ModuleUtils.add(new NoRotate());
        ModuleUtils.add(new bunnyHop());
        ModuleUtils.add(new InvMove());

        ModuleUtils.add(new ItemESP());

        ModuleUtils.add(new PlayerTracer());
        ModuleUtils.add(new EntityTracer());

        ModuleUtils.add(new NoWeather());
        ModuleUtils.add(new FreeCam());
        ModuleUtils.add(new XRay());
        ModuleUtils.add(new HighCameraDistance());
        ModuleUtils.add(new NoCameraClip());
        ModuleUtils.add(new NoSwing());
        ModuleUtils.add(new OverWorldXYZ());
        ModuleUtils.add(new XYZ());
        ModuleUtils.add(new Ping());
        ModuleUtils.add(new FPS());
        ModuleUtils.add(new InvNether());
        ModuleUtils.add(new NoHurtCam());
        ModuleUtils.add(new RandomName());
        ModuleUtils.add(new NameTag());
        ModuleUtils.add(new BigNameTag());
        ModuleUtils.add(new DeathPosDisplay());
        ModuleUtils.add(new PlayerPosesDisplay());
        ModuleUtils.add(new ItemDisplay());
        ModuleUtils.add(new PlayerDisplay());
        ModuleUtils.add(new ChatUp());

        ModuleUtils.add(new NoNetherOverlay());
        ModuleUtils.add(new NoHnadOverlay());
        ModuleUtils.add(new NoPowderSnowOverlay());
        ModuleUtils.add(new NoShieldOverlay());
        ModuleUtils.add(new NoNauseaOverlay());
        ModuleUtils.add(new NoDarknessOverlay());
        ModuleUtils.add(new ShaderOverlay());
        ModuleUtils.add(new NoBackground());

        ModuleUtils.add(new AutoSwitch());
        ModuleUtils.add(new AutoSteal());
        ModuleUtils.add(new AutoStore());
        ModuleUtils.add(new AntiSpam());
        ModuleUtils.add(new DontClearMessages());
        ModuleUtils.add(new ASRP());
        ModuleUtils.add(new DarkTitleBar());

        CommandUtils.add(new BindCommand());
        CommandUtils.add(new setCommand());
        CommandUtils.add(new listModulesCommand());

        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("SimpleClient modules loaded.");
    }

    @SubscribeEvent
    public void click(PlayerInteractEvent.RightClickBlock event){
        if (!event.getEntity().isShiftKeyDown() && ModuleUtils.get(ClickTp.class.getSimpleName()).enabled && event.getEntity().level().getBlockState(new BlockPos(event.getPos().getX(), event.getPos().getY()+1, event.getPos().getZ())).isAir() && Minecraft.getInstance().getConnection() != null) {
            Vec3 pos = new Vec3(event.getPos().getX(), event.getPos().getY()+1, event.getPos().getZ());
            event.getEntity().moveTo(pos);
            event.getEntity().setPos(event.getPos().getX(), event.getPos().getY()+1, event.getPos().getZ());
            Minecraft.getInstance().getConnection().getConnection().send(new ServerboundMovePlayerPacket.Pos(event.getPos().getX(), event.getPos().getY()+1, event.getPos().getZ(), event.getEntity().onGround()));
        }
    }

    @SubscribeEvent
    public void click(PlayerInteractEvent.RightClickEmpty event){
        if (!event.getEntity().isShiftKeyDown() && (event.getEntity().getItemInHand(event.getEntity().getUsedItemHand()).getItem() instanceof BlockItem) && ModuleUtils.get(AirPlace.class.getSimpleName()).enabled && Minecraft.getInstance().getConnection() != null && Minecraft.getInstance().hitResult!=null) {
            Minecraft.getInstance().getConnection().getConnection().send(new ServerboundUseItemOnPacket(event.getEntity().getUsedItemHand(),(BlockHitResult) Minecraft.getInstance().hitResult,3));
        }
    }

    @SubscribeEvent
    public void mine(PlayerInteractEvent.LeftClickBlock event){
        if (!event.getEntity().isShiftKeyDown() && ModuleUtils.get(FastMine.class.getSimpleName()).enabled && Minecraft.getInstance().getConnection() != null && Minecraft.getInstance().hitResult!=null) {
            if (Minecraft.getInstance().gameMode != null) {
                if (Minecraft.getInstance().player != null) {
                    if (Minecraft.getInstance().hitResult != null) {
                        Objects.requireNonNull(Minecraft.getInstance().getConnection()).getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,event.getPos(),Minecraft.getInstance().player.getDirection()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void update(MovementInputUpdateEvent event){
        if (ModuleUtils.enabled(InvMove.class.getSimpleName())) {
            Minecraft MC = Minecraft.getInstance();
            KeyMapping.releaseAll();
            event.getInput().up = InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyUp.getKey().getValue());
            event.getInput().down = InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyDown.getKey().getValue());
            event.getInput().left = InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyLeft.getKey().getValue());
            event.getInput().right = InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyRight.getKey().getValue());

            Minecraft.getInstance().options.keyUp.setDown(InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyUp.getKey().getValue()));
            Minecraft.getInstance().options.keyDown.setDown(InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyDown.getKey().getValue()));
            Minecraft.getInstance().options.keyLeft.setDown(InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyLeft.getKey().getValue()));
            Minecraft.getInstance().options.keyRight.setDown(InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyRight.getKey().getValue()));

            event.getInput().shiftKeyDown = InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyShift.getKey().getValue());
            event.getInput().jumping = InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keyJump.getKey().getValue());
            event.getEntity().setSprinting(InputConstants.isKeyDown(MC.getWindow().getWindow(),MC.options.keySprint.getKey().getValue()));
        }
    }
}
