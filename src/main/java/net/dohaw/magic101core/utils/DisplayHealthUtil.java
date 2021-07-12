package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class DisplayHealthUtil {

    private static Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();


    public static void updateHealth(Profile profile, Player player){

        int curHealth = profile.getHealth().getCurrentHealth();

        Objective objective = board.getObjective("health");

        if(objective == null){
            objective = board.registerNewObjective("health", "dummy", ChatColor.RED + "❤", RenderType.HEARTS);
        }
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.getScore(player.getName()).setScore(curHealth);

        player.setLevel(curHealth);
    }

}
