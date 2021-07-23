package net.dohaw.magic101core.commands;

import net.dohaw.magic101core.utils.MenuUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ProfileSelectCommand implements CommandExecutor {

    JavaPlugin plugin;
    public ProfileSelectCommand(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;


        MenuUtil.openProfileSelectionMenu(plugin,player);

        return true;
    }
}
