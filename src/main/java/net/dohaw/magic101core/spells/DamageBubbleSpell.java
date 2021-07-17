package net.dohaw.magic101core.spells;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageBubbleSpell extends Spell {

    public DamageBubbleSpell(Location location, Player player, JavaPlugin plugin) {
        super(location, player, plugin);
    }

    @Override
    public void cast() {
        create15BlockRadius(Color.GRAY);
        player.sendMessage("You have cast Damage Bubble");

        applyBuff("damage", 0.20, 30);
    }
}