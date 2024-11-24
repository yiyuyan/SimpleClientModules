package cn.ksmcbrigade.scm.commands;

import cn.ksmcbrigade.scb.SimpleClientBase;
import cn.ksmcbrigade.scb.command.Command;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class listModulesCommand extends Command {
    public listModulesCommand() {
        super("list-m");
    }

    @Override
    public void onCommand(Minecraft MC, Player player, String[] args) throws Exception {
        player.sendSystemMessage(Component.literal("Modules: "));
        ArrayList<Module> modules = new ArrayList<>(SimpleClientBase.modules);

        player.sendSystemMessage(CommonComponents.NEW_LINE);

        player.sendSystemMessage(Component.literal(ModuleType.COMBAT.name+": "));
        modules.stream().filter(m -> m.type.name.equals((ModuleType.COMBAT.name))).forEach(m->player.sendSystemMessage(Component.literal(m.name)));
        player.sendSystemMessage(CommonComponents.NEW_LINE);

        player.sendSystemMessage(Component.literal(ModuleType.MOVEMENT.name+": "));
        modules.stream().filter(m -> m.type.name.equals((ModuleType.MOVEMENT.name))).forEach(m->player.sendSystemMessage(Component.literal(m.name)));
        player.sendSystemMessage(CommonComponents.NEW_LINE);

        player.sendSystemMessage(Component.literal(ModuleType.BLOCK.name+": "));
        modules.stream().filter(m -> m.type.name.equals((ModuleType.BLOCK.name))).forEach(m->player.sendSystemMessage(Component.literal(m.name)));
        player.sendSystemMessage(CommonComponents.NEW_LINE);

        player.sendSystemMessage(Component.literal(ModuleType.RENDER.name+": "));
        modules.stream().filter(m -> m.type.name.equals((ModuleType.RENDER.name))).forEach(m->player.sendSystemMessage(Component.literal(m.name)));
        player.sendSystemMessage(CommonComponents.NEW_LINE);

        player.sendSystemMessage(Component.literal(ModuleType.OVERLAY.name+": "));
        modules.stream().filter(m -> m.type.name.equals((ModuleType.OVERLAY.name))).forEach(m->player.sendSystemMessage(Component.literal(m.name)));
        player.sendSystemMessage(CommonComponents.NEW_LINE);

        player.sendSystemMessage(Component.literal(ModuleType.MISC.name+": "));
        modules.stream().filter(m -> m.type.name.equals((ModuleType.MISC.name))).forEach(m->player.sendSystemMessage(Component.literal(m.name)));
        player.sendSystemMessage(CommonComponents.NEW_LINE);
    }
}
