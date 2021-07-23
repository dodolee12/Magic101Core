package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class DisplayHealthUtil {

    private static Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

    public static void updateHealth(Profile profile, Player player){
        //updateProfile(profile, player);
        updatePlayerHealth(profile, player);
    }


    public static void updatePlayerHealth(Profile profile, Player player){

        int curHealth = profile.getHealth().getCurrentHealth();
        int maxHealth = profile.getHealth().getMaxHealth();

        Objective objective = board.getObjective("health");

        if(objective == null){
            objective = board.registerNewObjective("health", "dummy", ChatColor.RED + "❤", RenderType.HEARTS);
        }
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.getScore(player.getName()).setScore(curHealth);

        double percentHealth = ((double) curHealth / (double) maxHealth);
        int healthToSet = (int) (percentHealth*20);
        player.setHealth(healthToSet);
    }

    public static void updateHealth(Entity entity, int health){
        Objective objective = board.getObjective("health");

        if(objective == null){
            objective = board.registerNewObjective("health", "dummy", ChatColor.RED + "❤", RenderType.HEARTS);
        }
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.getScore(entity.getUniqueId().toString()).setScore(health);

    }

    public static void updateProfile(Profile profile, Player player){
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("stats","dummy","Stats:");

        int curHealth = profile.getHealth().getCurrentHealth();

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("Current Health: ").setScore(curHealth);
        objective.getScore("Maximum Health: ").setScore(curHealth);
        for(String buff: profile.getBuffNames()){
            if(profile.getBuff(buff) != 0){
                objective.getScore(buff + " buff (+%):").setScore((int) (100 * profile.getBuff(buff)));
            }
        }

        player.setScoreboard(board);

    }
}
