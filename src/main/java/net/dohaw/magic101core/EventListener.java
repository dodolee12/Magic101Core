package net.dohaw.magic101core;

import net.dohaw.magic101core.items.CustomItem;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.profile.ProfileSelectionMenu;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.runnables.LingeringDamageRunnable;
import net.dohaw.magic101core.runnables.StunPlayerRunnable;
import net.dohaw.magic101core.runnables.UpdateItemsRunnable;
import net.dohaw.magic101core.spells.Spell;
import net.dohaw.magic101core.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

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
        ItemStack itemInHand = e.getItem();

        if(!itemIsUsable(player, activeProfile, itemInHand)){
            player.sendMessage("You do not meet the class requirement to use this item");
            e.setCancelled(true);
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){

            PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(itemInHand);

            if(pdc == null){
                return;
            }

            String spellName = PropertyHelper.getStringFromPDC(pdc, "spell-name");

            if(spellName == null || spellName.equals("None")){
                return;
            }

            Location playerLocation = player.getLocation();

            ItemProperties aggregatedProperties = DamageHelper.getDamageProps(player);

            Spell spell = Constants.getSpellFromName(spellName, playerLocation, aggregatedProperties, player, plugin);

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

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        e.setDamage(0);
        Entity attacker = e.getDamager();
        Entity attacked = e.getEntity();

        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player attackerPlayer = (Player) attacker;
            Profile attackerProfile = ALL_PROFILES.findActiveProfile(attackerPlayer.getUniqueId());
            if(!itemIsUsable(attackerPlayer, attackerProfile, attackerPlayer.getInventory().getItemInMainHand())){
                e.setCancelled(true);
                return;
            }
            DamageHelper.playerDamagePlayerHandler(attackerPlayer, (Player) attacked, plugin);
        }

    }


    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
        activeProfile.saveProfile(player);

        player.getInventory().clear();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    ProfileSelectionMenu profileSelectionMenu = new ProfileSelectionMenu(plugin);
                    profileSelectionMenu.initializeItems(player);
                    profileSelectionMenu.openInventory(player);
                    ALL_PROFILES.PROFILES_IN_SELECTION.add(player.getUniqueId());
        }
                , 3);
    }

    private boolean itemIsUsable(Player player, Profile profile, ItemStack item){

        PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(item);

        if(pdc == null){
            return true;
        }

        String schoolName = PropertyHelper.getStringFromPDC(pdc,"school");

        if(schoolName == null){
            return true;
        }

        Schools school = Schools.valueOf(schoolName);

        if(school != Schools.UNIVERSAL && school != profile.getSchool()) {
            return false;
        }

        return true;
    }


}
