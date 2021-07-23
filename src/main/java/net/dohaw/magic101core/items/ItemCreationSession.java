package net.dohaw.magic101core.items;

import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ItemCreationSession {

    private String key = "None";
    private String displayName = "Not set";
    private List<String> lore = new ArrayList<>();
    private Material material = Material.APPLE;
    private ItemProperties itemProperties = new ItemProperties();
    private Schools school = Schools.UNIVERSAL;
    private String spellName = "None";

    public ItemCreationSession(){
    }

    public ItemCreationSession(CustomItem item){
        key = item.getKEY();
        displayName = item.getDisplayName();
        lore = item.getLore();
        material = item.getMaterial();
        itemProperties = item.getItemProperties();
        school = item.getSchool();
        spellName = item.getSpellName();
    }

    public CustomItem toCustomItem(){
        return new CustomItem(key,displayName,lore,material,itemProperties,school,spellName);
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ItemProperties getItemProperties() {
        return itemProperties;
    }

    public void setItemProperties(ItemProperties itemProperties) {
        this.itemProperties = itemProperties;
    }

    public void setKey(String KEY) {
        this.key = KEY;
    }

    public Schools getSchool() {
        return school;
    }

    public void setSchool(Schools school) {
        this.school = school;
    }

    public String getSpellName() {
        return spellName;
    }

    public void setSpellName(String spellName) {
        this.spellName = spellName;
    }
}
