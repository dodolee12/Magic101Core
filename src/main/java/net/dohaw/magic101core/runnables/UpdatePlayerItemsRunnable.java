package net.dohaw.magic101core.runnables;

import net.dohaw.corelib.StringUtils;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdatePlayerItemsRunnable extends BukkitRunnable {

    private Player player;
    private final ItemStack PROFILE_VIEW_BOOK;

    public UpdatePlayerItemsRunnable(Player player, ItemStack book){
        this.player = player;
        PROFILE_VIEW_BOOK = book;
    }
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        Inventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        for(int i = 0; i < items.length; ++i){
            ItemStack itemStack = items[i];
            if(itemStack == null){
                continue;
            }
            ItemMeta meta = itemStack.getItemMeta();
            if(meta == null){
                continue;
            }

            if(checkIfProfileChange(itemStack)){
                if(i != 8){
                    inventory.setItem(i,new ItemStack(Material.AIR));
                }
                inventory.setItem(8,itemStack);

                return;
            }

            if(meta.getPersistentDataContainer().has(NamespacedKey.minecraft("key"), PersistentDataType.STRING)){
                String key = meta.getPersistentDataContainer().get(NamespacedKey.minecraft("key"), PersistentDataType.STRING);
                if(!ALL_ITEMS.ALL_ITEMS_MAP.containsKey(key)){
                    inventory.setItem(i,new ItemStack(Material.AIR));
                    continue;
                }
                inventory.setItem(i,ALL_ITEMS.ALL_ITEMS_MAP.get(key).toItemStack());
            }
        }
        inventory.setItem(8,PROFILE_VIEW_BOOK);
    }

    private boolean checkIfProfileChange(ItemStack itemInHand){
        return itemInHand.getType().equals(Material.BOOK) &&
                itemInHand.getItemMeta().getDisplayName().equals(StringUtils.colorString("&eRight Click to View Profile"));
    }

}