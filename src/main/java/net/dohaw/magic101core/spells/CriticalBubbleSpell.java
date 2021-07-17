package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CriticalBubbleSpell extends Spell {

    public CriticalBubbleSpell(Location location, Player player, JavaPlugin plugin) {
        super(location, player, plugin);
    }

    @Override
    public void cast() {
        create15BlockRadius(Color.WHITE);
        player.sendMessage("You have cast Critical Bubble");

        applyBuff("critical strike chance", 0.05, 30);
    }
}
