package com.mimo.Commands;

import com.mimo.City;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {
    public static void registerCommands() {
        Commands.literal("city").then(Commands.literal("create").executes(CommandManager::cityCommandSended)).executes(CommandManager::cityCreateCommandSended);
        // TODO: Implement the city command
    }

    public static int cityCommandSended(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getExecutor() instanceof Player player) {
            if (!(City.playerCityHashMap.containsKey(player))) {
                player.sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>"));
            }
        }
        return 0;
    }

    public static int cityCreateCommandSended(CommandContext<CommandSourceStack> ctx) {
        return 0;
    }
}
