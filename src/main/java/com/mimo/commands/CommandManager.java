package com.mimo.commands;

import com.mimo.City;
import com.mimo.War;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;

import java.util.List;

// TODO: Enter sounds on click in gui or smt idk
public class CommandManager {
    public CommandManager(Commands commands) {
        // WOOOOOOW BRIGADIER IS SOO COOL!!!!!
        commands.register(
                Commands.literal("city")
                        .then(
                                Commands.literal("create")
                                        .then(Commands.argument("name", StringArgumentType.word())
                                                .executes(City::cityCreateCommandExecute)
                                        )
                        )
                        .then(
                                Commands.literal("claim")
                                        .executes(City::cityClaimCommandExecute)
                        )
                        .then(
                                Commands.literal("join")
                                        .then(Commands.argument("name", StringArgumentType.word())
                                                .suggests(City::cityJoinCommandSuggest)
                                                .executes(City::cityJoinCommandExecute)
                                        )
                        )
                        .then(
                                Commands.literal("war")
                                        .executes(War::cityWarCommandExecute)
                                        .then(
                                                Commands.literal("start")
                                        ).executes(ctx -> {
                                            ctx.getSource().getExecutor().sendMessage(Component.text("error: enter in this format: /city war start <war type> <city>", net.kyori.adventure.text.format.NamedTextColor.RED));
                                            return 0;
                                        })
                                        .then(Commands.argument("wartype", StringArgumentType.word())
                                                .suggests(War::warTypesSuggest)
                                                .executes(ctx -> {
                                                    ctx.getSource().getExecutor().sendMessage(Component.text("error: enter an attacker city name", net.kyori.adventure.text.format.NamedTextColor.RED));
                                                    return 0;
                                                })
                                        )
                                        .then(Commands.argument("city", StringArgumentType.word())
                                                .suggests(War::warCitiesSuggest)
                                                .executes(War::cityStartWarCommandExecute)
                                        )
                                        .then(Commands.literal("allies"))
                                        .executes(War::warAlliesCommandExecute)
                                        .then(Commands.literal("inviteally")
                                                .then(Commands.argument("city", StringArgumentType.word())
                                                        .suggests(War::warCitiesSuggest)
                                                        .executes(War::warInviteAllyCommandExecute)
                                                )
                                        )
                                        .then(Commands.literal("removeally")
                                                .then(Commands.argument("city", StringArgumentType.word())
                                                        .suggests(War::warCitiesSuggest)
                                                        .executes(War::warRemoveAllyCommandExecute)
                                                )
                                        )
                                        .then(Commands.literal("list")
                                                .executes(War::warListCommandExecute)
                                        )
                                        .then(Commands.literal("enemies")
                                                .executes(War::warEnemiesCommandExecute)
                                        )
                                        .then(Commands.literal("reviewally")
                                                .then(Commands.argument("inviter", StringArgumentType.word())
                                                        .then(Commands.argument("invitee", StringArgumentType.word())
                                                                .then(Commands.argument("attacker", StringArgumentType.word())
                                                                        .then(Commands.argument("defender", StringArgumentType.word())
                                                                                .executes(War::warReviewAllyCommandExecute)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                // Allies and stuff fully implemented
                        )
                        .executes(City::cityCommandExecute).build(), "Manage Cities", List.of("c")
        );
    }
}
