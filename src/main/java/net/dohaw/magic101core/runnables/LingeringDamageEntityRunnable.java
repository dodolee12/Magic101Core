package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.utils.DisplayHealthUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class LingeringDamageEntityRunnable extends BukkitRunnable {

    private LivingEntity attacked;
    private int lingeringDamage;
    private int timesToDamage = 5;

    public LingeringDamageEntityRunnable(LivingEntity attacked, int lingeringDamage){
        this.attacked = attacked;
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
            return;
        }
        --timesToDamage;
        attacked.setHealth(attacked.getHealth() - lingeringDamage);
        DisplayHealthUtil.updateHealth(attacked,(int) attacked.getHealth());

    }
}