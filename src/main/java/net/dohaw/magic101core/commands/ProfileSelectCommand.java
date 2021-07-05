package net.dohaw.magic101core.commands;

import net.dohaw.magic101core.menus.ProfileSelectionMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ProfileSelectCommand implements CommandExecutor {

    JavaPlugin plugin;
    public ProfileSelectCommand(JavaPlugin claimPlugin){
        this.plugin = claimPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;


        ProfileSelectionMenu profileSelectionMenu = new ProfileSelectionMenu(plugin);
        profileSelectionMenu.initializeItems(player);
        profileSelectionMenu.openInventory(player);

        return true;
    }
}
