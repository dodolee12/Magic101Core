package net.dohaw.magic101core.items;

import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Material;

import java.util.List;

public class CustomItem {

    private final String KEY;
    private String displayName;
    private List<String> lore;
    private Material material;
    private ItemProperties itemProperties;
    private Schools school;
    private String spellName;

    public CustomItem(String key, String displayName, List<String> lore, Material material, ItemProperties itemProperties, Schools school, String spellName) {
        KEY = key;
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.itemProperties = itemProperties;
        this.school = school;
        this.spellName = spellName;
    }
}
