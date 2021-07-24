package net.dohaw.magic101core.menus.profile;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.menus.items.CreateItemMenu;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.utils.Constants;
import net.dohaw.magic101core.utils.MenuUtil;
import net.dohaw.magic101core.utils.PropertyHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewMenu extends Menu implements Listener{

    private Profile profile;

    public ProfileViewMenu(JavaPlugin plugin, Profile profile) {
        super(plugin, null, "Profile Menu", 45);
        JPUtils.registerEvents(this);
        this.profile = profile;
    }

    @Override
    public void initializeItems(Player player) {

        Schools school = profile.getSchool();
        List<String> profileLore = new ArrayList<String>(){{
                add("&cCharacter name: &e" + profile.getCharacterName());
                add("&cLevel: " + profile.getLevel());
                add("Health: " + profile.getHealth().getCurrentHealth() + " / " + profile.getHealth().getMaxHealth());
            }};
        List<String> propLore = PropertyHelper.getAggregatedItemProperties(player).getTotalPropLoreWithoutLevel();


        inv.setItem(13,createGuiItem(Material.PLAYER_HEAD,"Your Profile",profileLore));
        inv.setItem(21,createGuiItem(Constants.schoolsToMaterial.get(school),
                "&cClass: &e" + profile.getSchool().toString(),new ArrayList<>()));
        inv.setItem(23, createGuiItem(Material.DIAMOND_CHESTPLATE, "Stats from Armor", propLore));

        inv.setItem(inv.getSize() - 1, createGuiItem(Material.BARRIER,"Change Profile",new ArrayList<>()));

        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        fillMenu(false);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        int slotClicked = e.getSlot();
        if(e.getClickedInventory() == null) return;
        if(!e.getClickedInventory().equals(inv)) return;
        e.setCancelled(true);
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || clickedItem.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) return;

        if(slotClicked == inv.getSize() - 1){
            player.closeInventory();
            MenuUtil.openProfileSelectionMenu(plugin,player);
        }
    }
}
