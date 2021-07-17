package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.items.CustomItem;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.runnables.LingeringDamageRunnable;
import net.dohaw.magic101core.runnables.StunPlayerRunnable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class DamageHelper {

    private static Random RNG = new Random();

    public static void playerDamagePlayerHandler(Player attacker, Player attacked, JavaPlugin plugin){

        Profile attackerActiveProfile = ALL_PROFILES.findActiveProfile(attacker.getUniqueId());
        Profile attackedActiveProfile = ALL_PROFILES.findActiveProfile(attacked.getUniqueId());

        ItemProperties damageProps = getDamageProps(attacker);

        if(damageProps == null){
            return;
        }

        //calculate damage
        int damage = getDamage(damageProps,attackerActiveProfile);

        //check if crit
        damage = applyCritChance(damage,damageProps,attackerActiveProfile);

        //check if stun
        boolean stun = checkStun(damageProps);

        //lingering chance
        boolean lingering = checkLingering(damageProps);

        //TODO check if lingering healing affects lifesteal

        //attacked stats
        ItemProperties damagedPlayerProps = PropertyHelper.getAggregatedItemProperties(attacked);

        //defense + pierce
        damage = applyDefenseAndPierce(damage, damageProps, damagedPlayerProps, attackerActiveProfile, attackedActiveProfile);

        int lifeToSteal = (int) (damage * (damageProps.getLifesteal()/100));

        //healing and damage
        attackerActiveProfile.getHealth().heal(lifeToSteal);
        attackedActiveProfile.getHealth().damage(damage);
        if(attackedActiveProfile.getHealth().isDead()){
            attackedActiveProfile.getHealth().healToFull();
            attacked.setHealth(0);
        }

        DisplayHealthUtil.updateHealth(attackerActiveProfile,attacker);
        DisplayHealthUtil.updateHealth(attackedActiveProfile,attacked);

        //stun player
        if(stun){
            //stun runnable
            new StunPlayerRunnable(attacked, attacked.getLocation()).runTaskTimer(plugin,1,2);
            attacker.sendMessage("Your attack stunned the target");
            attacked.sendMessage("You have been stunned");
        }

        if(lingering){
            int lingeringDamage = (int) (damage * (damageProps.getLingeringDamage()/100));
            new LingeringDamageRunnable(attacked, attackedActiveProfile, lingeringDamage).runTaskTimer(plugin,20,20);
            attacker.sendMessage("Your attack lingered");
            attacked.sendMessage("You have been attacked by a lingering attack");
        }

    }

    public static ItemProperties getDamageProps(Player attacker){
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if(weapon.getItemMeta() == null){
            return null;
        }

        if(!CustomItem.canCreateCustomItem(weapon)){
            return null;
        }

        ItemProperties weaponProps = new CustomItem(weapon).getItemProperties();
        ItemProperties attackerProps = PropertyHelper.getAggregatedItemProperties(attacker);

        return ItemProperties.addTwoItemProperties(weaponProps, attackerProps);
    }

    private static int getDamage(ItemProperties damageProps, Profile profile){
        return (int) (damageProps.getDamage() * (1 + profile.getBuff("damage"))); // possibility of user in damage bubble
    }

    //input: damage in
    //output: damage after applying crit (if needed)
    private static int applyCritChance(int damage, ItemProperties damageProps, Profile profile){
        double critChance = damageProps.getCritChance()/100 + profile.getBuff("critical strike chance");
        if(critChance > 0 && RNG.nextDouble() < critChance){
            damage *= 2;
        }
        return damage;
    }

    private static boolean checkStun(ItemProperties damageProps){
        double stunChance = damageProps.getStunChance()/100;

        return stunChance > 0 && RNG.nextDouble() < stunChance;
    }

    private static boolean checkLingering(ItemProperties damageProps){
        double lingeringChance = damageProps.getLingeringChance()/100;

        return lingeringChance > 0 && RNG.nextDouble() < lingeringChance;
    }

    //input damage, output damage after defense and pierce
    private static int applyDefenseAndPierce(int damage, ItemProperties damageProps, ItemProperties damagedPlayerProps,
                                             Profile attackerProfile, Profile attackedProfile){
        double rawDefense = damagedPlayerProps.getDefense()/100 + attackedProfile.getBuff("resist");
        double pierce = damageProps.getPierce()/100 + attackerProfile.getBuff("pierce");
        double netDefense = rawDefense - pierce;
        if(netDefense < 0){
            netDefense = 0;
        }
        return (int) (damage * (1 - netDefense));
    }
}
