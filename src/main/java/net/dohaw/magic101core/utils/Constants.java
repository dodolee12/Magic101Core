package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.spells.LifeBubbleSpell;
import net.dohaw.magic101core.spells.Spell;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
        put(Schools.ICE,"Resist bubble");
        put(Schools.LIFE,"Life bubble");
        put(Schools.DEATH,"Pierce bubble");
        put(Schools.BALANCE,"Critical Bubble");
        put(Schools.STORM,"Damage bubble");
        put(Schools.FIRE,"Fire Lingering AOE");
        put(Schools.MYTH,"Minion");
    }};

    public static Spell getSpellFromName(String spellName, Location location, ItemProperties itemProperties, Player player){
        switch (spellName){
            case "Life bubble":
                return new LifeBubbleSpell(location, player, itemProperties.getOutgoingHealing());
        }
        return null;
    }
}

