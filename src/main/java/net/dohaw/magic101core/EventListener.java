package net.dohaw.magic101core;

import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.profile.ProfileSelectionMenu;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.spells.Spell;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.Constants;
import net.dohaw.magic101core.utils.DisplayHealthUtil;
import net.dohaw.magic101core.utils.PropertyHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;

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
        ALL_PROFILES.PROFILES_IN_SELECTION.add(player.getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        if(ALL_PROFILES.PROFILES_IN_SELECTION.contains(player.getUniqueId())){
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.sendMessage("You have tried to exit profile creation. You have been redirected to the profile selection screen.");
                ProfileSelectionMenu profileSelectionMenu = new ProfileSelectionMenu(plugin);
                profileSelectionMenu.initializeItems(player);
                profileSelectionMenu.openInventory(player);
            });
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Profile profile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
        if(profile == null){
            return;
        }
        profile.saveProfile(player);
        player.getInventory().clear();
    }

    @EventHandler
    public void gainXP(PlayerExpChangeEvent e){
        e.setAmount(0);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            ItemStack itemInHand = e.getItem();

            PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(itemInHand);

            if(pdc == null){
                return;
            }

            String schoolName = PropertyHelper.getStringFromPDC(pdc,"school");

            if(schoolName == null){
                return;
            }

            Schools school = Schools.valueOf(schoolName);

            if(school != activeProfile.getSchool()) {
                player.sendMessage("This item is only equippable by " + school + " class");
                e.setCancelled(true);
                return;
            }

            String spellName = PropertyHelper.getStringFromPDC(pdc, "spell-name");

            if(spellName == null || spellName.equals("None")){
                return;
            }

            Location playerLocation = player.getLocation();

            ItemProperties aggregatedProperties = new ItemProperties();

            //aggregate properties

            Spell spell = Constants.getSpellFromName(spellName, playerLocation, aggregatedProperties, player);

            spell.cast();

        }
    }

    //@EventHandler
//    public void onInventoryClickEvent(InventoryClickEvent e){
//        Player player = (Player) e.getWhoClicked();
//        Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
//
//        ItemStack item = e.getCurrentItem();
//        PersistentDataContainer pdc = getPDCFromItem(item);
//
//        if(pdc == null){
//            return;
//        }
//
//        String schoolName = getStringFromPDC(pdc,"school");
//
//        if(schoolName == null){
//            return;
//        }
//
//        Schools school = Schools.valueOf(schoolName);
//
//        if(school != activeProfile.getSchool()) {
//            player.sendMessage("This item is only equippable by " + school + " class");
//            e.setCancelled(true);
//            return;
//        }
//    }


}
