package net.dohaw.magic101core;

import net.dohaw.magic101core.menus.profile.ProfileSelectionMenu;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {

    private JavaPlugin plugin;

    public EventListener(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.getInventory().clear();
        ProfileSelectionMenu profileSelectionMenu = new ProfileSelectionMenu(plugin);
        profileSelectionMenu.initializeItems(player);
        profileSelectionMenu.openInventory(player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        Inventory inventory = e.getInventory();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Profile profile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
        if(profile == null){
            return;
        }
        profile.saveProfile(player);
    }
}
