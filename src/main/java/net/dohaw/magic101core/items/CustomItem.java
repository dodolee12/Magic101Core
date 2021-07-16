package net.dohaw.magic101core.items;

import net.dohaw.corelib.StringUtils;
import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
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

    public CustomItem(ItemStack itemStack){
        PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
        KEY = pdc.get(NamespacedKey.minecraft("key"), PersistentDataType.STRING);
        this.school = Schools.valueOf(pdc.get(NamespacedKey.minecraft("school"), PersistentDataType.STRING));
        this.spellName = pdc.get(NamespacedKey.minecraft("spell-name"), PersistentDataType.STRING);
        this.displayName = itemStack.getItemMeta().getDisplayName();
        this.lore = itemStack.getItemMeta().getLore() == null ? new ArrayList<String>() : itemStack.getItemMeta().getLore();
        this.material = itemStack.getType();

        int level = pdc.get(NamespacedKey.minecraft("level"), PersistentDataType.INTEGER);
        int damage = pdc.get(NamespacedKey.minecraft("damage"), PersistentDataType.INTEGER);
        int maxHealth = pdc.get(NamespacedKey.minecraft("max-health"), PersistentDataType.INTEGER);
        double pierce = pdc.get(NamespacedKey.minecraft("pierce"), PersistentDataType.DOUBLE);
        double critChance = pdc.get(NamespacedKey.minecraft("crit-chance"), PersistentDataType.DOUBLE);
        double stunChance = pdc.get(NamespacedKey.minecraft("stun-chance"), PersistentDataType.DOUBLE);
        double defense = pdc.get(NamespacedKey.minecraft("defense"), PersistentDataType.DOUBLE);
        double lifesteal = pdc.get(NamespacedKey.minecraft("lifesteal"), PersistentDataType.DOUBLE);
        double lingeringChance = pdc.get(NamespacedKey.minecraft("lingering-chance"), PersistentDataType.DOUBLE);
        int lingeringDamage = pdc.get(NamespacedKey.minecraft("lingering-damage"), PersistentDataType.INTEGER);
        double outgoingHealing = pdc.get(NamespacedKey.minecraft("outgoing-healing"), PersistentDataType.DOUBLE);
        double incomingHealing = pdc.get(NamespacedKey.minecraft("incoming-healing"), PersistentDataType.DOUBLE);
        this.itemProperties = new ItemProperties(level, damage, maxHealth, pierce, critChance, stunChance,
                defense, lifesteal, lingeringChance, lingeringDamage, outgoingHealing, incomingHealing);
    }

    public ItemStack toItemStack(){
        String itemDisplayname = StringUtils.colorString(displayName);
        List<String> itemlore = StringUtils.colorLore(lore);
        String coloredString = StringUtils.colorString("&cONLY EQUIPPABLE BY " + school.toString());
        if(lore.size() == 0 || !lore.get(lore.size() - 1).equals(coloredString)){
            itemlore.add(coloredString);
        }
        ItemStack item;
        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if(!itemDisplayname.equalsIgnoreCase("None")){
            meta.setDisplayName(itemDisplayname);
        }

        if(!itemlore.isEmpty()){
            meta.setLore(itemlore);
        }

        item.setItemMeta(setAllPDCValues(meta));
        return item;
    }

    private ItemMeta setAllPDCValues(ItemMeta meta){
        //store key, itemprops, school, spellname
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(NamespacedKey.minecraft("key"), PersistentDataType.STRING, KEY);
        pdc.set(NamespacedKey.minecraft("school"), PersistentDataType.STRING, school.toString());
        pdc.set(NamespacedKey.minecraft("spell-name"), PersistentDataType.STRING, spellName);

        pdc.set(NamespacedKey.minecraft("level"), PersistentDataType.INTEGER, itemProperties.getLevel());
        pdc.set(NamespacedKey.minecraft("damage"), PersistentDataType.INTEGER, itemProperties.getDamage());
        pdc.set(NamespacedKey.minecraft("max-health"), PersistentDataType.INTEGER, itemProperties.getMaxHealth());
        pdc.set(NamespacedKey.minecraft("pierce"), PersistentDataType.DOUBLE, itemProperties.getPierce());
        pdc.set(NamespacedKey.minecraft("crit-chance"), PersistentDataType.DOUBLE, itemProperties.getCritChance());
        pdc.set(NamespacedKey.minecraft("stun-chance"), PersistentDataType.DOUBLE, itemProperties.getStunChance());
        pdc.set(NamespacedKey.minecraft("defense"), PersistentDataType.DOUBLE, itemProperties.getDefense());
        pdc.set(NamespacedKey.minecraft("lifesteal"), PersistentDataType.DOUBLE, itemProperties.getLifesteal());
        pdc.set(NamespacedKey.minecraft("lingering-chance"), PersistentDataType.DOUBLE, itemProperties.getLingeringChance());
        pdc.set(NamespacedKey.minecraft("lingering-damage"), PersistentDataType.INTEGER, itemProperties.getLingeringDamage());
        pdc.set(NamespacedKey.minecraft("outgoing-healing"), PersistentDataType.DOUBLE, itemProperties.getOutgoingHealing());
        pdc.set(NamespacedKey.minecraft("incoming-healing"), PersistentDataType.DOUBLE, itemProperties.getIncomingHealing());

        return meta;
    }

    public String getKEY() {
        return KEY;
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
