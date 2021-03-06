package net.dohaw.magic101core.menus.profile;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.runnables.UpdatePlayerItemsRunnable;
import net.dohaw.magic101core.utils.ALL_PROFILES;
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
import java.util.UUID;

public class ProfileSelectionMenu extends Menu implements Listener {

    private final ItemStack PROFILE_VIEW_BOOK = createGuiItem(Material.BOOK, "&eRight Click to View Profile", new ArrayList<>());

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
                List<String> lore = new ArrayList<String>(){{
                    if(profile.isActive()){
                        add("Currently Active Profile");
                    }
                    add("&cCharacter name: &e" + profile.getCharacterName());
                    add("&cClass: &e" + profile.getSchool().toString());
                    add("&cLevel: " + profile.getLevel() + ", Health: " + profile.getHealth().getMaxHealth());
                    add("");
                    add("&7Left-Click to Select");
                    add("&7Right-Click to Edit");
                    add("&7Shift+Right-Click to Delete");
                }};
                inv.setItem(i, createGuiItem(profile.isActive() ? Material.SKELETON_SKULL : Material.PLAYER_HEAD, profile.getProfileName(), lore));
            }
        }
        //TODO restrict profile number based on rank
        inv.setItem(profileList == null ? 22 : 40, createGuiItem(Material.STICK, "&eCreate new Profile", new ArrayList<>()));

        inv.setItem(inv.getSize() - 1, createGuiItem(Material.BARRIER, "&eLeave Server", new ArrayList<>()));

        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        fillMenu(false);

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

        UUID playerUUID = player.getUniqueId();
        String profileName = clickedItem.getItemMeta().getDisplayName();
        List<Profile> profileList = ALL_PROFILES.ALL_PROFILES_MAP.get(player.getUniqueId());
        ALL_PROFILES.PROFILES_IN_SELECTION.remove(player.getUniqueId());

        int leaveServerSlot = inv.getSize() - 1;

        if((profileList != null && slotClicked == 40) || (profileList == null && slotClicked == 22)){
            Menu newMenu = new ProfileCreationMenu(plugin, new ProfileCreationSession(), true, 0, this);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
            ALL_PROFILES.PROFILES_IN_SELECTION.add(player.getUniqueId());
        }
        else if(slotClicked == leaveServerSlot){
            player.kickPlayer("Left Server");
        }
        else if(e.getClick().isShiftClick() && e.getClick().isRightClick()){
            //remove profile
            Profile profileToRemove = ALL_PROFILES.getProfileByProfileName(playerUUID,profileName);
            ALL_PROFILES.ALL_PROFILES_MAP.get(playerUUID).remove(profileToRemove);
            this.clearItems();
            player.closeInventory();
            this.initializeItems(player);
            this.openInventory(player);
            ALL_PROFILES.PROFILES_IN_SELECTION.add(player.getUniqueId());
        }
        else if(e.getClick().isRightClick()){
            Profile profile = ALL_PROFILES.getProfileByProfileName(playerUUID,profileName);
            Menu newMenu = new ProfileCreationMenu(plugin, profile.getSession(), false, slotClicked, this);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
            ALL_PROFILES.PROFILES_IN_SELECTION.add(player.getUniqueId());
        }
        else if(e.getClick().isLeftClick()){

            Profile oldProfile = ALL_PROFILES.findActiveProfile(playerUUID);
            if(oldProfile != null){
                oldProfile.saveProfile(player);
            }

            Profile newProfile = ALL_PROFILES.getProfileByProfileName(playerUUID,profileName);
            if(newProfile.isActive()){
                player.closeInventory();
                return;
            }
            newProfile.loadProfile(player);
            player.closeInventory();
            new UpdatePlayerItemsRunnable(player,PROFILE_VIEW_BOOK).runTask(plugin);
            player.sendMessage("You have selected the " + profileName + " profile.");
        }

    }
}
