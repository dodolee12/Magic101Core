package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.utils.PlayerLocationHelper;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ResistBubbleSpell extends Spell{

    public ResistBubbleSpell(Location location, Player player, JavaPlugin plugin) {
        super(location, player, plugin);
    }

    @Override
    public void cast() {
        create15BlockRadius(Color.BLUE);
        player.sendMessage("You have cast Resist Bubble");

        applyBuff("resist", 0.20, 30);
    }
}
