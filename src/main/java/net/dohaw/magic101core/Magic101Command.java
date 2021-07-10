package net.dohaw.magic101core;

import net.dohaw.magic101core.menus.items.CustomItemsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Magic101Command implements CommandExecutor {

    private JavaPlugin plugin;

    public Magic101Command(JavaPlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0){
            helpCommand(sender);
            return false;
        }

        // player commands
        if(sender instanceof Player){

            Player pSender = (Player) sender;
            String firstArg = args[0];
            switch(firstArg){
                case "itemcreate":
                    itemCreationCommand(pSender);
                    break;

                case "customitems":
                    CustomItemsMenu customItemsMenu = new CustomItemsMenu(plugin);
                    customItemsMenu.initializeItems(pSender);
                    customItemsMenu.openInventory(pSender);
                    break;

                    //TODO add help
            }

        }

        return false;
    }

    private void helpCommand(CommandSender sender){}

    private void itemCreationCommand(Player sender){



    }

}
