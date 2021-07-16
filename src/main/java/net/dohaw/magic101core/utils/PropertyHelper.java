package net.dohaw.magic101core.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PropertyHelper {

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
