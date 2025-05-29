package com.mimo;

import it.unimi.dsi.fastutil.Hash;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class City {
    public static HashMap<Player, City> playerCityHashMap = new HashMap<>();
    private List<Chunk> chunks = new ArrayList<>();
    @Setter
    private String name;
    @Setter
    private Player owner;
    private List<Player> players = new ArrayList<>();

    public City(String name, Player owner) {
        this.name = name;
        this.owner = owner;
        players.add(owner);
        playerCityHashMap.put(owner, this);
    }

    // TODO: Implement methods add Players/Revmove

    public static City getCityByPlayer(Player player) {
        return playerCityHashMap.get(player);
    }
}