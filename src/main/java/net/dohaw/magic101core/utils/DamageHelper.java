package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.runnables.LingeringDamageEntityRunnable;
import net.dohaw.magic101core.runnables.LingeringDamagePlayerRunnable;
import net.dohaw.magic101core.runnables.StunEntityRunnable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DamageHelper {

    private static Random RNG = new Random();

    public static void damageHandler(LivingEntity attacker, LivingEntity attacked, JavaPlugin plugin){
        Profile attackerActiveProfile = null;
        Profile attackedActiveProfile = null;
        if(attacker instanceof Player){
            attackerActiveProfile = ALL_PROFILES.findActiveProfile(attacker.getUniqueId());
        }
        if(attacked instanceof Player){
            attackedActiveProfile = ALL_PROFILES.findActiveProfile(attacked.getUniqueId());
        }
        ItemProperties damageProps = PropertyHelper.getAggregatedItemProperties(attacker);

        if(damageProps == null){
            return;
        }

        //calculate damage
        int damage = getDamage(damageProps,attackerActiveProfile);

        Map<Schools,Integer> classDamages = getClassDamages(damageProps);

        Schools damageClass = Schools.UNIVERSAL;
        //add class damages
        for(Schools school: classDamages.keySet()){
            damage += classDamages.get(school);
            damageClass = school;
        }

        //add strength
        damage = applyStrength(damage,damageProps,damageClass);

        //check if crit
        damage = applyCritChance(damage,damageProps,attackerActiveProfile, damageClass);

        //check if stun
        boolean stun = checkStun(damageProps);

        //lingering chance
        boolean lingering = checkLingering(damageProps);

        //attacked stats
        ItemProperties damagedPlayerProps = PropertyHelper.getAggregatedItemProperties(attacked);

        //defense + pierce
        damage = applyDefenseAndPierce(damage, damageProps, damagedPlayerProps, attackerActiveProfile, attackedActiveProfile, damageClass);

        //incoming healing affects lifesteal

        int lifeToSteal = (int) (damage * (damageProps.getLifesteal()/100) * (1 + damageProps.getIncomingHealing()/100));

        //healing and damage
        handleAttackerHeal(attacker,lifeToSteal,attackerActiveProfile);
        handleAttacked(attacked,damage,attackedActiveProfile);

        //stun player
        if(stun){
            //stun runnable
            new StunEntityRunnable(attacked, attacked.getLocation()).runTaskTimer(plugin,1,2);
            attacker.sendMessage("Your attack stunned the target");
            attacked.sendMessage("You have been stunned");
        }
        if(lingering){
            int lingeringDamage = (int) (damage * (damageProps.getLingeringDamage()/100));
            if(attacked instanceof Player){
                new LingeringDamagePlayerRunnable((Player) attacked, attackedActiveProfile, lingeringDamage).runTaskTimer(plugin,20,20);
            }
            else{
                new LingeringDamageEntityRunnable(attacked,lingeringDamage).runTaskTimer(plugin,20,20);
            }
            attacked.sendMessage("You have been attacked by a lingering attack");
            attacker.sendMessage("Your attack lingered");
        }


    }

    private static Map<Schools,Integer> getClassDamages(ItemProperties damageProps){
        Map<Schools, Integer> classDamages = new HashMap<>();
        for(Schools school: Schools.values()){
            String fieldName = StringUtil.capitalizeFirstLetter(school.toString().toLowerCase()) + " Damage";
            if(damageProps.getClassProperty(fieldName) != 0){
                classDamages.put(school,(int) damageProps.getClassProperty(fieldName));
                return classDamages;
            }
        }
        return classDamages;
    }

    private static void handleAttackerHeal(LivingEntity attacker, int lifeToSteal, Profile attackerActiveProfile){
        if(attacker instanceof Player){
            attackerActiveProfile.getHealth().heal(lifeToSteal);
            DisplayHealthUtil.updateHealth(attackerActiveProfile, (Player) attacker);
        }
        else{
            int health = (int) (attacker.getHealth() + lifeToSteal);
            attacker.setHealth(health);
            DisplayHealthUtil.updateHealth(attacker, health);
        }
    }

    private static void handleAttacked(LivingEntity attacked, int damage, Profile attackedActiveProfile){
        if(attacked instanceof Player){
            attackedActiveProfile.getHealth().damage(damage);
            if(attackedActiveProfile.getHealth().isDead()){
                attackedActiveProfile.getHealth().healToFull();
                attacked.setHealth(0);
            }

            DisplayHealthUtil.updateHealth(attackedActiveProfile,(Player) attacked);

        }
        else{
            int health = (int) (attacked.getHealth() - damage);
            if(health < 0){
                health = 0;
            }
            attacked.setHealth(health);
            DisplayHealthUtil.updateHealth(attacked, health);
        }


    }

    private static int getDamage(ItemProperties damageProps, Profile profile){
        if(profile == null){
            return damageProps.getDamage();
        }
        return (int) (damageProps.getDamage() * (1 + profile.getBuff("Damage"))); // possibility of user in damage bubble
    }

    //input: damage in
    //output: damage after applying crit (if needed)
    private static int applyCritChance(int damage, ItemProperties damageProps, Profile profile, Schools damageClass){
        double critChance = damageProps.getCritChance()/100;
        if(profile != null){
            critChance += profile.getBuff("Critical strike chance");
        }

        if(damageClass != Schools.UNIVERSAL){
            String critFieldName = StringUtil.capitalizeFirstLetter(damageClass.toString().toLowerCase()) + " Critical Rating";
            critChance += damageProps.getClassProperty(critFieldName)/100;
        }

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
                                             Profile attackerProfile, Profile attackedProfile, Schools damageClass){


        double rawDefense = damagedPlayerProps.getDefense()/100;
        if(attackedProfile != null){
            rawDefense += attackedProfile.getBuff("Defense");
        }
        double pierce = damageProps.getPierce()/100;
        if(attackerProfile != null){
            pierce += attackerProfile.getBuff("Pierce");
        }

        if(damageClass != Schools.UNIVERSAL){
            String defenseFieldName = StringUtil.capitalizeFirstLetter(damageClass.toString().toLowerCase()) + " Resist";
            String pierceFieldName = StringUtil.capitalizeFirstLetter(damageClass.toString().toLowerCase()) + " Pierce";

            rawDefense += damageProps.getClassProperty(defenseFieldName)/100;
            pierce += damageProps.getClassProperty(pierceFieldName)/100;
        }

        double netDefense = rawDefense - pierce;
        if(netDefense < 0){
            netDefense = 0;
        }
        return (int) (damage * (1 - netDefense));
    }

    private static int applyStrength(int damage, ItemProperties damageProps, Schools damageClass){
        double strength = damageProps.getStrength()/100;

        if(damageClass != Schools.UNIVERSAL){
            String strengthFieldName = StringUtil.capitalizeFirstLetter(damageClass.toString().toLowerCase()) + " Strength";
            strength += damageProps.getClassProperty(strengthFieldName)/100;
        }

        return (int) (damage * (1 + strength));
    }
}
