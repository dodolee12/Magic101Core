package net.dohaw.magic101core.commands;

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
                case "ci":

                case "customitems":
                    if(!pSender.isOp()){
                        pSender.sendMessage("You do not have the permissions for this command");
                        return true;
                    }

                    CustomItemsMenu customItemsMenu = new CustomItemsMenu(plugin);
                    customItemsMenu.initializeItems(pSender);
                    customItemsMenu.openInventory(pSender);
                    break;

                    //TODO add help
                case "help":
                    pSender.sendMessage("/magic101 customitems: Bring up custom item create/edit menu.");
                    break;
            }

        }

        return false;
    }

    private void helpCommand(CommandSender sender){}


}
