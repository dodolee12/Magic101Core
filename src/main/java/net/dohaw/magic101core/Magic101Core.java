package net.dohaw.magic101core;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import net.dohaw.magic101core.commands.Magic101Command;
import net.dohaw.magic101core.commands.ProfileSelectCommand;
import net.dohaw.magic101core.config.BaseHealthsConfig;
import net.dohaw.magic101core.config.ItemConfig;
import net.dohaw.magic101core.config.MaterialConfig;
import net.dohaw.magic101core.config.ProfileConfig;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.runnables.UpdateItemsRunnable;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.jeff_media.armorequipevent.ArmorEquipEvent;



public final class Magic101Core extends JavaPlugin {

    private ProfileConfig profileConfig;
    private ItemConfig itemConfig;
    private BaseHealthsConfig baseHealthsConfig;
    private MaterialConfig materialConfig;

    @Override
    public void onEnable() {
        ArmorEquipEvent.registerListener(this);
        CoreLib.setInstance(this);
        validateConfigs();
        loadCustomConfigs();
        fixItemsRunnable();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory true");

        JPUtils.registerEvents(new EventListener(this));
        JPUtils.registerCommand("profile",new ProfileSelectCommand(this));
        JPUtils.registerCommand("magic101", new Magic101Command(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveAllProfiles();

        saveCustomConfigs();
    }

    private void fixItemsRunnable(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new UpdateItemsRunnable(), 5, 5*20);
    }

    private void saveAllProfiles(){
        for(Player player: Bukkit.getOnlinePlayers()){
            Profile profile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
            if(profile == null){
                continue;
            }
            profile.saveProfile(player);
            player.kickPlayer("Server Restart");
        }
    }

    private void validateConfigs(){
        profileConfig = new ProfileConfig(this);
        itemConfig = new ItemConfig(this);
        baseHealthsConfig = new BaseHealthsConfig(this);
        materialConfig = new MaterialConfig(this);

        profileConfig.valdiateConfig();
        itemConfig.valdiateConfig();
        baseHealthsConfig.valdiateConfig();
        materialConfig.valdiateConfig();
    }

    private void loadCustomConfigs(){
        profileConfig.loadConfig();
        itemConfig.loadConfig();
        baseHealthsConfig.loadConfig();
        materialConfig.loadConfig();
    }

    private void saveCustomConfigs(){
        profileConfig.saveConfig();
        itemConfig.saveConfig();
    }

}
