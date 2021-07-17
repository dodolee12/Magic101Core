package net.dohaw.magic101core.runnables;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StunPlayerRunnable extends BukkitRunnable {

    private Player player;
    private Location stunLocation;
    private int timesToStun = 30;

    public StunPlayerRunnable(Player player, Location location){
        this.player = player;
        this.stunLocation = location;
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
        if(timesToStun <= 0){
            cancel();
        }
        --timesToStun;
        player.teleport(stunLocation);
    }
}
