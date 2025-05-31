package com.mimo;

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

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            playerCityHashMap.put(player, this);
        }
    }

    public void removePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            playerCityHashMap.remove(player);
        }
    }

    public static City getCityByPlayer(Player player) {
        return playerCityHashMap.get(player);
    }
}