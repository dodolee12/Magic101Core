package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.runnables.LifeBubbleRunnable;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.PlayerLocationHelper;
import net.dohaw.magic101core.utils.PropertyHelper;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class LifeBubbleSpell extends Spell {

    private double outgoingHealing;
    private final int DURATION = 10;


    public LifeBubbleSpell(Location location, Player player, JavaPlugin plugin, double outgoingHealing) {
        super(location, player, plugin);
        this.outgoingHealing = outgoingHealing;
    }

    @Override
    public void cast() {
        create15BlockRadius(Color.GREEN, DURATION);
        player.sendMessage("You have cast Life Bubble");

        new LifeBubbleRunnable(outgoingHealing,location).runTaskTimer(plugin,1,20);

    }

}
