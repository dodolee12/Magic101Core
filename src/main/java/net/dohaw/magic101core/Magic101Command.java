package net.dohaw.magic101core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Magic101Command implements CommandExecutor {



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
            }

        }

        return false;
    }

    private void helpCommand(CommandSender sender){}

    private void itemCreationCommand(Player sender){



    }

}
