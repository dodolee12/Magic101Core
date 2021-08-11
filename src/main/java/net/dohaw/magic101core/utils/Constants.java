package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.spells.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Constants {
    public static Map<Schools, Integer> schoolToBaseHealth = new HashMap<Schools, Integer>();


    public static Map<Schools, String> schoolsToSpellName = new HashMap<Schools,String>(){{
        put(Schools.ICE,"Resist Bubble");
        put(Schools.LIFE,"Life Bubble");
        put(Schools.DEATH,"Pierce Bubble");
        put(Schools.BALANCE,"Critical Bubble");
        put(Schools.STORM,"Damage Bubble");
        put(Schools.FIRE,"Fire Lingering AOE");
        put(Schools.MYTH,"Minion");
    }};

    public static Map<Schools, Material> schoolsToMaterial = new HashMap<>();

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
            case "Fire Lingering AOE":
                return new FireLingeringSpell(location, player, plugin);
            case "Minion":
                return new MinionSpell(location, player, plugin);
        }
        return null;
    }

    public static Set<String> classProps = new HashSet<String>(){{
        add(" Damage");
        add(" Pierce");
        add(" Resist");
        add(" Critical Rating");
        add(" Strength");
    }};

    //cd in sec
    public static Map<String,Integer> spellToCooldown = new HashMap<String,Integer>(){{
        put("Resist Bubble",60);
        put("Life Bubble",60);
        put("Pierce Bubble",60);
        put("Critical Bubble",60);
        put("Damage Bubble",60);
        put("Fire Lingering AOE",60);
        put("Minion",60);
    }};
}

