package net.dohaw.magic101core.menus;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.profiles.ALL_PROFILES;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.ProfileCreationSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ProfileSelectionMenu extends Menu implements Listener {

    private JavaPlugin plugin;

    public ProfileSelectionMenu(JavaPlugin plugin) {
        super(plugin, null, "Profile Selection", 45);
        JPUtils.registerEvents(this);
        this.plugin = plugin;
    }

    @Override
    public void initializeItems(Player player) {
        List<Profile> profileList = ALL_PROFILES.ALL_PROFILES_MAP.get(player.getUniqueId());
        if(profileList != null){
            for(int i = 0; i < profileList.size(); ++i){
                Profile profile = profileList.get(i);
                List<String> lore = new ArrayList<>();
                lore.add("Character name: " + profile.getCharacterName());
                lore.add("Class: " + profile.getSchool().toString());
                lore.add(profile.getStats().toString());
                inv.setItem(i, createGuiItem(Material.PLAYER_HEAD, profile.getProfileName(), lore));
            }
        }
        //TODO restrict profile number based on rank
        inv.setItem(38, createGuiItem(Material.STICK, "&eCreate new Profile", new ArrayList<>()));

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
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        Menu newMenu = null;
        switch(slotClicked){
            case 38:
                newMenu = new ProfileCreationMenu(plugin, new ProfileCreationSession());
                break;
        }

        if(newMenu != null){
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }
    }
}
