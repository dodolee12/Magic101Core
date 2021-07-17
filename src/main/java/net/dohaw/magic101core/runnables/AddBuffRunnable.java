package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AddBuffRunnable extends BukkitRunnable {

    private Profile profile;
    private String buffName;
    private double amount;

    public AddBuffRunnable(Player player, String buffName, double amount){
        this.buffName = buffName;
        this.amount = amount;
        this.profile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
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
        profile.addBuff(buffName,amount);
    }
}
