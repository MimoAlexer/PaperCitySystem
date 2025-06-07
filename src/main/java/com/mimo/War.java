package com.mimo;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.Getter;

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
        return 0; // TODO: Implement city war logic
    }

    public static CompletableFuture<Suggestions> warTypesSuggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("tributary");
        actions.add("raid");
        actions.add("conquest");
        actions.forEach(builder::suggest);
        return builder.buildFuture();
    }
}
