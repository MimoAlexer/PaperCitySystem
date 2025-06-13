package com.mimo;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Getter
public class War {
    City attacker;
    City defender;
    ArrayList<City> attackerAllies = new ArrayList<>();
    ArrayList<City> defenderAllies = new ArrayList<>();
    WarTypes warType;
    int attackerScore = 0;
    int defenderScore = 0;

    public War(City attacker, City defender, WarTypes warType) {
        this.attacker = attacker;
        this.defender = defender;
        this.warType = warType;
        attacker.wars.add(this);
        defender.wars.add(this);
    }

    public void addAttackerAlly(City ally) {
        if (!attackerAllies.contains(ally)) {
            attackerAllies.add(ally);
        }
    }

    public void addDefenderAlly(City ally) {
        if (!defenderAllies.contains(ally)) {
            defenderAllies.add(ally);
        }
    }

    public static int cityWarCommandExecute(CommandContext<CommandSourceStack> ctx) {
        if (City.playerCityHashMap.get(ctx.getSource().getExecutor()) == null) {
            ctx.getSource().getExecutor().sendMessage(Component.text("You are not in a city! Create one with /city create <name>, or join an existing city, with /city join <name>"));
            return 0;
        }
        WarTypes warType = WarTypes.getWarType(ctx.getArgument("wartype", String.class));
        City defenderCity = City.playerCityHashMap.get(ctx.getArgument("city", String.class));
        City attackerCity = City.playerCityHashMap.get(ctx.getSource().getExecutor());
        for (War war : attackerCity.getWars()) {
            if (war.getDefender().equals(defenderCity) && war.getAttacker().equals(attackerCity)) {
                ctx.getSource().getExecutor().sendMessage(Component.text("You are already at war with " + defenderCity.getName() + "!"));
                return 0;
            }
        }
        // TODO: add War GUI
        // TODO: add conformation GUI
        switch (warType) {
            case WarTypes.TRIBUTARY:
                ctx.getSource().getExecutor().sendMessage(Component.text("You have started a tributary war!"));
            case WarTypes.RAID:
                ctx.getSource().getExecutor().sendMessage(Component.text("You have started a raid war!"));
            case WarTypes.CONQUEST:
                ctx.getSource().getExecutor().sendMessage(Component.text("You have started a conquest war!"));
                break;
            case null:
                ctx.getSource().getExecutor().sendMessage(Component.text("Enter a valid war type!"));
                break;
            default:
                ctx.getSource().getExecutor().sendMessage(Component.text("Unknown war type!"));
                return 0;
        }
        return 0;
    }

    public static CompletableFuture<Suggestions> warTypesSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("tributary");
        actions.add("raid");
        actions.add("conquest");
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> WarCitiesSuggest(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        City.cityArrayList.forEach(city -> actions.add(city.getName()));
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }
}
