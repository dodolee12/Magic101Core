package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.runnables.LifeBubbleRunnable;
import net.dohaw.magic101core.runnables.LingeringDamageRunnable;
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

    private final int BASE_HEAL = 100;

    public LifeBubbleSpell(Location location, Player player, JavaPlugin plugin, double outgoingHealing) {
        super(location, player, plugin);
        this.outgoingHealing = outgoingHealing;
    }

    @Override
    public void cast() {
        create15BlockRadius(Color.GREEN);
        player.sendMessage("You have cast Life Bubble");

        List<Player> playersToHeal = PlayerLocationHelper.getAllPlayersNBlocksFromLocation(location,15);

        for(Player player: playersToHeal){
            double incomingHealing = PropertyHelper.getAggregatedDoubleProperty(player,"incoming-healing");

            int amountToHeal = (int) (BASE_HEAL * (1 + (outgoingHealing/100)) * (1 + (incomingHealing/100)));

            //create a runnable to heal each player amount once a second
            Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
            new LifeBubbleRunnable(player, activeProfile, amountToHeal).runTaskTimer(plugin,1,20);
            player.sendMessage("You are being healed by life bubble");
        }

    }

}
