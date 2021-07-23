package net.dohaw.magic101core.spells;

import net.dohaw.corelib.JPUtils;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.runnables.FireLingeringSpellRunnable;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.PropertyHelper;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FireLingeringSpell extends Spell implements Listener {

    private final int DURATION = 10;
    private double pierce;
    private double critChance;
    private int damage;
    private boolean spellFired = false;

    public FireLingeringSpell(Location location, Player player, JavaPlugin plugin) {
        super(location, player, plugin);
        this.pierce = PropertyHelper.getAggregatedDoubleProperty(player,"pierce");
        this.critChance = PropertyHelper.getAggregatedDoubleProperty(player,"crit-chance");
        this.damage = PropertyHelper.getAggregatedIntegerProperty(player,"damage");
        JPUtils.registerEvents(this);
    }

    @Override
    public void cast() {
        player.sendMessage("You have cast Fire Lingering AOE");
        Location fireballLocationVector = player.getEyeLocation();
        World w = player.getWorld();
        Projectile fireball = w.spawn(fireballLocationVector, Fireball.class);
        fireball.setShooter(player);
        fireball.setBounce(false);
        fireball.setVelocity(fireballLocationVector.getDirection().multiply(1));
    }

    @EventHandler
    public void onFireBallHit(ProjectileHitEvent e){
        Entity fireball = e.getEntity();
        if(fireball instanceof Fireball) {
            Location hitLocation;
            if(e.getHitBlock() != null){
                hitLocation = e.getHitBlock().getLocation();
            }
            else{
                hitLocation = e.getHitEntity().getLocation();
            }
            if(spellFired){
                return;
            }
            new FireLingeringSpellRunnable(hitLocation, pierce, critChance, damage, DURATION).runTaskTimer(plugin,1,20);
            spellFired = true;
            location = hitLocation;
            create15BlockRadius(Color.RED, DURATION);
            fireball.remove();
            e.setCancelled(true);
        }
    }
}
