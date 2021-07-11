package net.dohaw.magic101core.menus.items;

import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.CustomItem;
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
import java.util.List;

public class DisplayItemsMenu extends Menu implements Listener {

    private final Material NEXT_PAGE_MAT = Material.STRING;
    private final Material LAST_PAGE_MAT = Material.LEVER;
    private final Material BACK_MAT = Material.ARROW;

    String currentKey;
    List<CustomItem> customItems;
    DisplayItemsMenu backPageMenu;


    public DisplayItemsMenu(JavaPlugin plugin, Menu customItemMenu, DisplayItemsMenu backPageMenu, String startKey) {
        super(plugin, customItemMenu, "Display Items", 45);
        this.currentKey = startKey;
        this.backPageMenu = backPageMenu;
        this.customItems = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player player) {

        for(int i = 0; i < 36; ++i){
            if(currentKey == null){
                break;
            }
            customItems.add(ALL_ITEMS.ALL_ITEMS_MAP.get(currentKey));
            currentKey = ALL_ITEMS.ALL_ITEMS_MAP.higherKey(currentKey);
        }

        for(int i = 0; i < customItems.size(); ++i){
            addCustomItemToGUI(i,customItems.get(i));
        }

        setBackMaterial(BACK_MAT);
        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        inv.setItem(inv.getSize() - 5, createBackButton());
        fillMenu(false);

        createBackPageButton();
        createNextPageButton();


    }

    private void addCustomItemToGUI(int invSlot, CustomItem item){
        inv.setItem(invSlot,createGuiItem(item.getMaterial(),item.getDisplayName(),item.getLore()));
    }

    private void createNextPageButton(){
        inv.setItem(inv.getSize() - 1, createGuiItem(NEXT_PAGE_MAT, "&eNext Page", new ArrayList<>()));
    }

    private void createBackPageButton(){
        inv.setItem(inv.getSize() - 9, createGuiItem(LAST_PAGE_MAT, "&ePrevious Page", new ArrayList<>()));
    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        int slotClicked = e.getSlot();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) return;

        int backPageSlot = inv.getSize() - 9;
        int prevMenuSlot = inv.getSize() - 5;
        int nextPageSlot = inv.getSize() - 1;

        Menu newMenu;

        if(slotClicked == prevMenuSlot){
            newMenu = previousMenu;
        }
        else if(slotClicked == backPageSlot){
            if(backPageMenu == null){
                return;
            }
            newMenu = backPageMenu;
        }
        else if(slotClicked == nextPageSlot){
            if(currentKey == null){
                return;
            }
            newMenu = new DisplayItemsMenu(plugin, previousMenu, this, currentKey);

        }
        else{
            newMenu = new EditItemMenu(plugin,this,customItems.get(slotClicked));
        }
        newMenu.initializeItems(player);
        player.closeInventory();
        newMenu.openInventory(player);


    }
}
