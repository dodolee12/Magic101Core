package net.dohaw.magic101core.menus.items;

import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class CustomItemsMenu extends Menu implements Listener {

    public CustomItemsMenu(JavaPlugin plugin) {
        super(plugin, null,"Item Creation", 45);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player p) {

        inv.setItem(20, createGuiItem(Material.STICK, "&eCreate new item", new ArrayList<>()));
        inv.setItem(24, createGuiItem(Material.BOOKSHELF, "&eEdit current items", new ArrayList<>()));

        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        fillMenu(false);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) return;

        Menu newMenu = null;
        switch(slotClicked){
            //create new item
            case 20:
                newMenu = new CreateItemMenu(plugin, this, new ItemCreationSession());
                break;
            // edit item
            case 24:
                newMenu = new DisplayItemsMenu(plugin, this, null, ALL_ITEMS.ALL_ITEMS_MAP.isEmpty() ? null : ALL_ITEMS.ALL_ITEMS_MAP.firstKey());
                break;
        }

        if(newMenu != null){
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }

    }

    public Menu getPreviousMenu(){
        return previousMenu;
    }
}