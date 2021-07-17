package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveBuffRunnable  extends BukkitRunnable {

    private Player player;
    private Profile profile;
    private String buffName;
    private double amount;

    public RemoveBuffRunnable(Player player, String buffName, double amount){
        this.player = player;
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
        profile.removeBuff(buffName,amount);
        player.sendMessage("Your buff has run out");
    }
}

