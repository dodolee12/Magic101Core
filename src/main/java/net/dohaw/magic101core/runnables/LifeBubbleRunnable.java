package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.utils.DisplayHealthUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LifeBubbleRunnable extends BukkitRunnable {

    private Player playerToHeal;
    private Profile profileToHeal;
    private int healAmount;
    private int timesToHeal = 10;

    public LifeBubbleRunnable(Player playerToHeal, Profile profileToHeal, int healAmount){
        this.playerToHeal = playerToHeal;
        this.profileToHeal = profileToHeal;
        this.healAmount = healAmount;
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

        profileToHeal.getHealth().heal(healAmount);
        DisplayHealthUtil.updateHealth(profileToHeal,playerToHeal);

    }
}
