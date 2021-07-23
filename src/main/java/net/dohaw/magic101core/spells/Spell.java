package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.runnables.UpdateBuffRunnable;
import net.dohaw.magic101core.runnables.SpawnSphereParticlesRunnable;
import net.dohaw.magic101core.utils.PlayerLocationHelper;
import net.dohaw.magic101core.utils.ShapeUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.List;

public abstract class Spell {

    protected Location location;
    protected Player player;
    protected JavaPlugin plugin;

    public Spell(Location location, Player player, JavaPlugin plugin) {
        this.location = location;
        this.player = player;
        this.plugin = plugin;
    }

    public abstract void cast();

    protected void create15BlockRadius(Color color, int seconds) {
        List<Location> sphere = ShapeUtil.generateSphere(location, 15, true);

        new SpawnSphereParticlesRunnable(sphere, color, seconds).runTaskTimer(plugin,1,20);
    }

    //all spells that give temp buffs will use this function
    //duration is in seconds
    protected void applyBuff(String buff, double amount, int duration){
        new UpdateBuffRunnable(location,buff,amount,duration).runTaskTimer(plugin,1,20);
    }
}
