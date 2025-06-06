package com.mimo;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class War {
    City attacker;
    City defender;
    ArrayList<City> attackerAllies = new ArrayList<>();
    ArrayList<City> defenderAllies = new ArrayList<>();
    int attackerScore = 0;
    int defenderScore = 0;

    public War(City attacker, City defender) {
        this.attacker = attacker;
        this.defender = defender;
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
}
