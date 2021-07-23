package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.DisplayHealthUtil;
import net.dohaw.magic101core.utils.PlayerLocationHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateBuffRunnable extends BukkitRunnable {

    private Location location;
    private String buffName;
    private double amount;
    private Map<UUID,Double> curBuffs = new HashMap<>();
    private final int startDuration;
    private int duration;
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public UpdateBuffRunnable(Location location, String buffName, double amount, int duration){
        this.buffName = buffName;
        this.amount = amount;
        this.location = location;
        this.duration = duration;
        startDuration = duration;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        if(duration == startDuration){
            for(Player player: Bukkit.getOnlinePlayers()){
                Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
                curBuffs.put(player.getUniqueId(),activeProfile.getBuff(buffName));
            }
        }

        if(duration <= 0){
            for(Player player: Bukkit.getOnlinePlayers()){
                Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
                UUID playerUUID = player.getUniqueId();
                if(activeProfile.getBuff(buffName) != curBuffs.get(playerUUID)){
                    player.sendMessage("The buff has run out");
                    activeProfile.removeBuff(buffName, amount);
                }
            }

            cancel();
            return;
        }
        --duration;
        for(Player player: Bukkit.getOnlinePlayers()){
            Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
            UUID playerUUID = player.getUniqueId();
            //check if player is inside circle
            if(PlayerLocationHelper.playerDistanceFromLocation(player, location) <= 15){
                //if player is already buffed do nothing, but if they are not, then buff
                if(activeProfile.getBuff(buffName) == curBuffs.get(playerUUID)){
                    player.sendMessage("You have entered a zone that increases your " +
                            buffName + " rating by " + decimalFormat.format(amount*100) + "%");
                    activeProfile.addBuff(buffName, amount);
                }
            }
            else{
                //if the player is buffed, then remove buff
                if(activeProfile.getBuff(buffName) != curBuffs.get(playerUUID)){
                    player.sendMessage("You have left the zone, the buff is no longer active");
                    activeProfile.removeBuff(buffName, amount);
                }
            }
            //DisplayHealthUtil.updateProfile(activeProfile, player);
        }

    }
}
