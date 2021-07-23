package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.DisplayHealthUtil;
import net.dohaw.magic101core.utils.PlayerLocationHelper;
import net.dohaw.magic101core.utils.PropertyHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LifeBubbleRunnable extends BukkitRunnable {

    private Location location;
    private double outgoingHealing;
    private int timesToHeal = 10;
    private final int BASE_HEAL = 100;

    public LifeBubbleRunnable(double outgoingHealing, Location location){
        this.outgoingHealing = outgoingHealing;
        this.location = location;
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
        if(timesToHeal <= 0){
            cancel();
        }
        --timesToHeal;

        List<Player> playersToHeal = PlayerLocationHelper.getAllPlayersNBlocksFromLocation(location,15);

        for(Player player: playersToHeal){
            double incomingHealing = PropertyHelper.getAggregatedDoubleProperty(player,"incoming-healing");

            int amountToHeal = (int) (BASE_HEAL * (1 + (outgoingHealing/100)) * (1 + (incomingHealing/100)));

            Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
            activeProfile.getHealth().heal(amountToHeal);
            DisplayHealthUtil.updateHealth(activeProfile,player);
            if(timesToHeal == 9){
                player.sendMessage("You are being healed by life bubble");
            }
        }



    }
}
