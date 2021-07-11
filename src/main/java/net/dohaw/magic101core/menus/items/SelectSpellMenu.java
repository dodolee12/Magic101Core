package net.dohaw.magic101core.menus.items;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemCreationSession;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.utils.Constants;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class SelectSpellMenu extends Menu implements Listener {

    private Schools school;
    private String spellName;
    private ItemCreationSession session;

    public SelectSpellMenu(JavaPlugin plugin, Menu prevMenu, Schools school, ItemCreationSession session){
        super(plugin, prevMenu, "Select Spell", 9);
        this.school = school;
        this.spellName = Constants.schoolsToSpell.get(school);
        this.session = session;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player player) {

        inv.setItem(2, createGuiItem(Material.BARRIER, "&eNo Spell", new ArrayList<>()));

        inv.setItem(6, createGuiItem(Material.STICK, "&e" + spellName, new ArrayList<>()));

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

        String spellname = "None";
        switch(slotClicked){
            case 2:
                spellname = "None";
                break;
            case 6:
                spellname = spellName;
                break;
        }
        session.setSpellName(spellname);
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
