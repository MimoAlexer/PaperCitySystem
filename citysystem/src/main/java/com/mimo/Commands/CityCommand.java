package com.mimo.Commands;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

@NullMarked
public class CityCommand implements BasicCommand{

    @Override
    public void execute(CommandSourceStack cStack, String[] args) {
        if(!(cStack.getExecutor() instanceof Player)) {
            return;
        }

        Player player = (Player) cStack.getExecutor();

        if(args.length == 0) {
            player.sendMessage("§cUsage: /city <args>");
            return;
        }        

        
    }
    
}
