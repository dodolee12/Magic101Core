package net.dohaw.magic101core.menus;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
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
                List<String> lore = new ArrayList<String>(){{
                    add("&cCharacter name: &e" + profile.getCharacterName());
                    add("&cClass: &e" + profile.getSchool().toString());
                    add(profile.getStats().toString());
                    add("");
                    add("&7Left-Click to Select");
                    add("&7Right-Click to Edit");
                    add("&7Shift+Right-Click to Delete");
                }};
                inv.setItem(i, createGuiItem(Material.PLAYER_HEAD, profile.getProfileName(), lore));
            }
        }
        //TODO restrict profile number based on rank
        inv.setItem(40, createGuiItem(Material.STICK, "&eCreate new Profile", new ArrayList<>()));

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
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        if(slotClicked == 40){
            Menu newMenu = new ProfileCreationMenu(plugin, new ProfileCreationSession(), true, 0, this);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }
        else if(e.getClick().isShiftClick() && e.getClick().isRightClick()){
            ALL_PROFILES.ALL_PROFILES_MAP.get(player.getUniqueId()).remove(slotClicked);
            player.closeInventory();
            this.initializeItems(player);
            this.openInventory(player);
        }
        else if(e.getClick().isRightClick()){
            Profile profile = ALL_PROFILES.ALL_PROFILES_MAP.get(player.getUniqueId()).get(slotClicked);
            Menu newMenu = new ProfileCreationMenu(plugin, profile.getSession(), false, slotClicked, this);
            newMenu.initializeItems(player);
            player.closeInventory();
            newMenu.openInventory(player);
        }

    }
}
