package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.utils.DisplayHealthUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LingeringDamageRunnable extends BukkitRunnable {

    private Profile attackedProfile;
    private Player attacked;
    private int lingeringDamage;
    private int timesToDamage = 5;

    public LingeringDamageRunnable(Player attacked, Profile attackedProfile, int lingeringDamage){
        this.attacked = attacked;
        this.attackedProfile = attackedProfile;
        this.lingeringDamage = lingeringDamage;
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
        if(timesToDamage <= 0){
            cancel();
        }
        --timesToDamage;
        attackedProfile.getHealth().damage(lingeringDamage);
        if(attackedProfile.getHealth().isDead()){
            attackedProfile.getHealth().healToFull();
            attacked.setHealth(0);
            cancel();
        }
        DisplayHealthUtil.updateHealth(attackedProfile,attacked);

    }
}
