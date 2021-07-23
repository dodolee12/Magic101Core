package net.dohaw.magic101core.runnables;

import net.dohaw.magic101core.config.BaseHealthsConfig;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.spells.FireLingeringSpell;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.DisplayHealthUtil;
import net.dohaw.magic101core.utils.PlayerLocationHelper;
import net.dohaw.magic101core.utils.PropertyHelper;
import org.bukkit.Location;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class FireLingeringSpellRunnable extends BukkitRunnable {

    private int duration;
    private final int BASE_DAMAGE;
    private double pierce;
    private double critChance;
    private Location location;
    private static Random RNG = new Random();

    public FireLingeringSpellRunnable(Location location, double pierce, double critChance, int damage, int duration){
        this.location = location;
        this.pierce = pierce;
        this.critChance = critChance;
        this.duration = duration;
        BASE_DAMAGE = 50 + damage;
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
        if(duration <= 0){
            cancel();
        }
        --duration;

        int damage = calculateDamage();
        List<Player> playersToDamage = PlayerLocationHelper.getAllPlayersNBlocksFromLocation(location,15);

        for(Player player: playersToDamage){
            double defense = PropertyHelper.getAggregatedDoubleProperty(player,"defense");

            damage = applyDefenseAndPierce(damage,defense);
            Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());

            if(activeProfile == null){
                return;
            }

            activeProfile.getHealth().damage(damage);
            if(activeProfile.getHealth().isDead()){
                activeProfile.getHealth().healToFull();
                player.setHealth(0);
                continue;
            }

            DisplayHealthUtil.updateHealth(activeProfile,player);

            if(duration == 9){
                player.sendMessage("You have been hit with a fire lingering AOE spell");
            }
        }
    }

    private int calculateDamage(){
        //apply crit chance
        int damage = BASE_DAMAGE;
        if(critChance > 0 && RNG.nextDouble() < critChance){
            damage *= 2;
        }
        return damage;
    }

    private int applyDefenseAndPierce(int damage, double defense){
        double netDefense = defense - pierce;
        if(netDefense < 0){
            netDefense = 0;
        }
        return (int) (damage * (1 - netDefense));
    }
}
