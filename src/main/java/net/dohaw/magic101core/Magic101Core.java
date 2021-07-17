package net.dohaw.magic101core;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import net.dohaw.magic101core.commands.Magic101Command;
import net.dohaw.magic101core.commands.ProfileSelectCommand;
import net.dohaw.magic101core.config.ItemConfig;
import net.dohaw.magic101core.config.ProfileConfig;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.runnables.UpdateItemsRunnable;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



public final class Magic101Core extends JavaPlugin {

    private ProfileConfig profileConfig;
    private ItemConfig itemConfig;

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);
        validateConfigs();
        loadCustomConfigs();
        fixItemsRunnable();

        JPUtils.registerEvents(new EventListener(this));
        JPUtils.registerCommand("profile",new ProfileSelectCommand(this));
        JPUtils.registerCommand("magic101", new Magic101Command(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        kickAllPlayers();

        saveCustomConfigs();
    }

    private void fixItemsRunnable(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new UpdateItemsRunnable(), 5, 5*20);
    }

    private void kickAllPlayers(){
        for(Player player: Bukkit.getOnlinePlayers()){
            player.kickPlayer("Server Restart");
            Profile profile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
            if(profile == null){
                continue;
            }
            profile.saveProfile(player);
            player.getInventory().clear();
        }
    }

    private void validateConfigs(){
        profileConfig = new ProfileConfig(this);
        itemConfig = new ItemConfig(this);

        profileConfig.valdiateConfig();
        itemConfig.valdiateConfig();
    }

    private void loadCustomConfigs(){
        profileConfig.loadConfig();
        itemConfig.loadConfig();
    }

    private void saveCustomConfigs(){
        profileConfig.saveConfig();
        itemConfig.saveConfig();
    }

}
