package net.dohaw.magic101core.menus.items;

import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class DisplayItemsMenu extends Menu implements Listener {

    private final Material NEXT_PAGE_MAT = Material.STRING;
    private final Material LAST_PAGE_MAT = Material.LEVER;
    private final Material BACK_MAT = Material.ARROW;

    int startNum;


    public DisplayItemsMenu(JavaPlugin plugin, Menu previousMenu, int startNum) {
        super(plugin, previousMenu, "Display Items", 45);
        this.startNum = startNum;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void initializeItems(Player player) {





        setBackMaterial(BACK_MAT);
        setFillerMaterial(Material.BLACK_STAINED_GLASS_PANE);
        inv.setItem(inv.getSize() - 5, createBackButton());
        fillMenu(false);

        createBackPageButton();
        createNextPageButton();


    }

    private void createNextPageButton(){
        inv.setItem(inv.getSize() - 1, createGuiItem(NEXT_PAGE_MAT, "&eNext Page", new ArrayList<>()));
    }

    private void createBackPageButton(){
        inv.setItem(inv.getSize() - 9, createGuiItem(LAST_PAGE_MAT, "&ePrevious Page", new ArrayList<>()));
    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent inventoryClickEvent) {

    }
}
