package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PierceBubbleSpell extends Spell {

    public PierceBubbleSpell(Location location, Player player, JavaPlugin plugin) {
        super(location, player, plugin);
    }

    @Override
    public void cast() {
        create15BlockRadius(Color.BLACK);
        player.sendMessage("You have cast Pierce Bubble");

        applyBuff("pierce", 0.15, 30);
    }
}
