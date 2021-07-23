package net.dohaw.magic101core.runnables;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SpawnSphereParticlesRunnable extends BukkitRunnable {

    private List<Location> sphere;
    private Color color;
    private int seconds;

    public SpawnSphereParticlesRunnable(List<Location> sphere, Color color, int seconds){
        this.sphere = sphere;
        this.color = color;
        this.seconds = seconds;
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
        if(seconds <= 0){
            cancel();
        }
        --seconds;

        for (Location location : sphere) {
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, new Particle.DustOptions(color, 1));
        }
    }
}
