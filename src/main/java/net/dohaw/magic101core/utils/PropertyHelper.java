package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.profiles.Profile;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PropertyHelper {

    public static ItemProperties getAggregatedItemProperties(LivingEntity entity){
        ItemProperties aggregatedProperties = new ItemProperties();

        ItemStack[] armor = entity instanceof Player ? ((Player) entity).getInventory().getArmorContents()
                : entity.getEquipment().getArmorContents();

        for(ItemStack item: armor){
            aggregatedProperties = updateItemProperties(aggregatedProperties, item);
        }

        ItemStack mainHandItem = entity instanceof Player ? ((Player) entity).getInventory().getItemInMainHand()
                : entity.getEquipment().getItemInMainHand();
        aggregatedProperties = updateItemProperties(aggregatedProperties, mainHandItem);

        return aggregatedProperties;
    }

    private static ItemProperties updateItemProperties(ItemProperties aggregatedProperties, ItemStack item){
        PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(item);

        if(pdc == null){
            return aggregatedProperties;
        }

        aggregatedProperties.setDamage(aggregatedProperties.getDamage() + getIntegerFromPDC(pdc, "damage"));
        aggregatedProperties.setMaxHealth(aggregatedProperties.getMaxHealth() + getIntegerFromPDC(pdc, "max-health"));
        aggregatedProperties.setPierce(aggregatedProperties.getPierce() + getDoubleFromPDC(pdc, "pierce"));
        aggregatedProperties.setCritChance(aggregatedProperties.getCritChance() + getDoubleFromPDC(pdc, "crit-chance"));
        aggregatedProperties.setStunChance(aggregatedProperties.getStunChance() + getDoubleFromPDC(pdc, "stun-chance"));
        aggregatedProperties.setDefense(aggregatedProperties.getDefense() + getDoubleFromPDC(pdc, "defense"));
        aggregatedProperties.setLifesteal(aggregatedProperties.getLifesteal() + getDoubleFromPDC(pdc, "lifesteal"));
        aggregatedProperties.setLingeringChance(aggregatedProperties.getLingeringChance() + getDoubleFromPDC(pdc, "lingering-chance"));
        aggregatedProperties.setLingeringDamage(aggregatedProperties.getLingeringDamage() + getDoubleFromPDC(pdc, "lingering-damage"));
        aggregatedProperties.setOutgoingHealing(aggregatedProperties.getOutgoingHealing() + getDoubleFromPDC(pdc, "outgoing-healing"));
        aggregatedProperties.setIncomingHealing(aggregatedProperties.getIncomingHealing() + getDoubleFromPDC(pdc, "incoming-healing"));

        return aggregatedProperties;
    }

    public static int getAggregatedIntegerProperty(Player player, String key){
        ItemStack[] armor = player.getInventory().getArmorContents();

        int aggregatedProperty = 0;

        for(ItemStack item: armor){
            PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(item);

            if(pdc == null){
                continue;
            }

            aggregatedProperty += getIntegerFromPDC(pdc,key);
        }

        ItemStack weapon = player.getEquipment().getItemInMainHand();

        PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(weapon);

        if(pdc != null){
            aggregatedProperty += getIntegerFromPDC(pdc,key);
        }

        return aggregatedProperty;
    }

    public static double getAggregatedDoubleProperty(Player player, String key){
        ItemStack[] armor = player.getInventory().getArmorContents();

        double aggregatedProperty = 0;

        for(ItemStack item: armor){
            PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(item);

            if(pdc == null){
                continue;
            }

            aggregatedProperty += getDoubleFromPDC(pdc,key);
        }

        ItemStack weapon = player.getEquipment().getItemInMainHand();

        PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(weapon);

        if(pdc != null){
            aggregatedProperty += getDoubleFromPDC(pdc,key);
        }

        return aggregatedProperty;
    }

    public static PersistentDataContainer getPDCFromItem(ItemStack item){
        if(item == null){
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if(meta == null){
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer();
    }

    public static String getStringFromPDC(PersistentDataContainer pdc, String key){
        if(!pdc.has(NamespacedKey.minecraft(key), PersistentDataType.STRING)){
            return null;
        }
        return pdc.get(NamespacedKey.minecraft(key), PersistentDataType.STRING);
    }

    public static int getIntegerFromPDC(PersistentDataContainer pdc, String key){
        if(!pdc.has(NamespacedKey.minecraft(key), PersistentDataType.INTEGER)){
            return 0;
        }
        return pdc.get(NamespacedKey.minecraft(key), PersistentDataType.INTEGER);
    }

    public static double getDoubleFromPDC(PersistentDataContainer pdc, String key){
        if(!pdc.has(NamespacedKey.minecraft(key), PersistentDataType.DOUBLE)){
            return 0;
        }
        return pdc.get(NamespacedKey.minecraft(key), PersistentDataType.DOUBLE);
    }
}
