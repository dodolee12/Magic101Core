package net.dohaw.magic101core.utils;

import net.dohaw.magic101core.menus.profile.ProfileSelectionMenu;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuUtil {

    public static void openProfileSelectionMenu(JavaPlugin plugin, Player player){
        ProfileSelectionMenu profileSelectionMenu = new ProfileSelectionMenu(plugin);
        profileSelectionMenu.initializeItems(player);
        ALL_PROFILES.PROFILES_IN_SELECTION.add(player.getUniqueId());
        profileSelectionMenu.openInventory(player);
    }
}
