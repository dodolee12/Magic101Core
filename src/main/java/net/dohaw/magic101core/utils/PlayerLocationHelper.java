package net.dohaw.magic101core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerLocationHelper {

    public static List<Player> getAllPlayersNBlocksFromLocation(Location location, int blocks){
        List<Player> playerList = new ArrayList<>();
        for(Player player: Bukkit.getOnlinePlayers()){
            if(playerDistanceFromLocation(player, location) <= blocks){
                playerList.add(player);
            }
        }
        return playerList;
    }

    public static double playerDistanceFromLocation(Player player, Location location){
        Location playerLocation = player.getLocation();
        return Math.sqrt(
                  Math.pow(playerLocation.getX() - location.getX(),2)
                + Math.pow(playerLocation.getY() - location.getY(),2)
                + Math.pow(playerLocation.getZ() - location.getZ(),2)
        );
    }
}
