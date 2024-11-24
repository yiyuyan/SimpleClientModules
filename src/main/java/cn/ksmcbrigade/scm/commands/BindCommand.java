package cn.ksmcbrigade.scm.commands;

import cn.ksmcbrigade.scb.command.Command;
import cn.ksmcbrigade.scb.guis.KeyboardSetting;
import cn.ksmcbrigade.scb.module.Module;
import cn.ksmcbrigade.scb.uitls.ModuleUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind",1);
    }

    @Override
    public void onCommand(Minecraft MC, Player player, String[] args) throws Exception {
        Module module = ModuleUtils.get(args[0]);
        if(module==null){
            player.sendSystemMessage(Component.literal("Can't find the module.").withStyle(ChatFormatting.RED));
        }
        else{
            Minecraft.getInstance().setScreen(new KeyboardSetting(module,null));
        }
    }
}
