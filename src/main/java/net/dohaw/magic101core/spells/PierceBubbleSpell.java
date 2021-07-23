package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PierceBubbleSpell extends Spell {

    private final int DURATION = 30;

    public PierceBubbleSpell(Location location, Player player, JavaPlugin plugin) {
        super(location, player, plugin);
    }

    @Override
    public void cast() {
        create15BlockRadius(Color.BLACK, DURATION);
        player.sendMessage("You have cast Pierce Bubble");

        applyBuff("Pierce", 0.15, DURATION);
    }
}
