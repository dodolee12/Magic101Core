package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.spells.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public static Map<Schools, Integer> schoolToBaseHealth = new HashMap<Schools, Integer>(){{
        put(Schools.ICE,800);
        put(Schools.LIFE,700);
        put(Schools.DEATH,650);
        put(Schools.BALANCE,650);
        put(Schools.STORM,400);
        put(Schools.FIRE,550);
        put(Schools.MYTH,700);
    }};

    public static Map<Schools, String> schoolsToSpellName = new HashMap<Schools,String>(){{
        put(Schools.ICE,"Resist Bubble");
        put(Schools.LIFE,"Life Bubble");
        put(Schools.DEATH,"Pierce Bubble");
        put(Schools.BALANCE,"Critical Bubble");
        put(Schools.STORM,"Damage Bubble");
        put(Schools.FIRE,"Fire Lingering AOE");
        put(Schools.MYTH,"Minion");
    }};

    public static Spell getSpellFromName(String spellName, Location location, ItemProperties itemProperties, Player player, JavaPlugin plugin){
        switch (spellName){
            case "Life Bubble":
                return new LifeBubbleSpell(location, player, plugin, itemProperties.getOutgoingHealing());
            case "Critical Bubble":
                return new CriticalBubbleSpell(location, player, plugin);
            case "Resist Bubble":
                return new ResistBubbleSpell(location, player, plugin);
            case "Pierce Bubble":
                return new PierceBubbleSpell(location, player, plugin);
            case "Damage Bubble":
                return new DamageBubbleSpell(location, player, plugin);
        }
        return null;
    }
}

