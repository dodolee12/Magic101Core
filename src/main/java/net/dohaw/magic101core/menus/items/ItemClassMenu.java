package net.dohaw.magic101core.menus.items;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.menus.profile.ProfileCreationMenu;
import net.dohaw.magic101core.profiles.ProfileCreationSession;
import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ItemClassMenu  extends Menu implements Listener {
    private ItemCreationSession session;

    public ItemClassMenu(JavaPlugin plugin, Menu previousMenu, ItemCreationSession session) {
        super(plugin, previousMenu, "Class Selection", 9);
        this.session = session;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player player) {
        int i = 0;
        for(Schools school :Schools.values()){
            inv.setItem(i, createGuiItem(Material.STICK, school.toString(), new ArrayList<>()));
            ++i;
        }
    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) return;

        Schools school = null;
        switch(clickedItem.getItemMeta().getDisplayName()){
            case "DEATH":
                school = Schools.DEATH;
                break;
            case "FIRE":
                school = Schools.FIRE;
                break;
            case "ICE":
                school = Schools.ICE;
                break;
            case "LIFE":
                school = Schools.LIFE;
                break;
            case "BALANCE":
                school = Schools.BALANCE;
                break;
            case "MYTH":
                school = Schools.MYTH;
                break;
            case "STORM":
                school = Schools.STORM;
                break;
            case "UNIVERSAL":
                school = Schools.UNIVERSAL;
                break;
        }
        session.setSpellName("None");
        session.setSchool(school);
        if(previousMenu instanceof  CreateItemMenu){
            ((CreateItemMenu) previousMenu).setSession(session);
        }else if(previousMenu instanceof EditItemMenu){
            ((EditItemMenu) previousMenu).setSession(session);
        }
        previousMenu.initializeItems(player);
        player.closeInventory();
        previousMenu.openInventory(player);

    }
}
