package net.dohaw.magic101core.config;

import net.dohaw.magic101core.items.CustomItem;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import net.dohaw.magic101core.utils.Constants;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BaseHealthsConfig {

    private File configFolder;
    private File baseHealthFile;

    private JavaPlugin plugin;

    public BaseHealthsConfig(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void valdiateConfig(){
        configFolder = new File(plugin.getDataFolder(),"config/");
        if(!configFolder.exists()){
            configFolder.mkdirs();
        }
        baseHealthFile = new File(configFolder,"basehealth.yml");

        if(!baseHealthFile.exists()){
            createDefault();
        }

    }

    public void loadConfig(){
        FileConfiguration baseHealthConfig = new YamlConfiguration();
        try {
            baseHealthConfig.load(baseHealthFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for(String key: baseHealthConfig.getKeys(false)){
            Schools school = Schools.valueOf(key);
            int baseHealth = baseHealthConfig.getInt(key);
            Constants.schoolToBaseHealth.put(school,baseHealth);
        }

    }

    private void createDefault(){
        FileConfiguration baseHealthConfig = new YamlConfiguration();

        try{
            baseHealthFile.createNewFile();
            baseHealthConfig.load(baseHealthFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        baseHealthConfig.set("MYTH", 700);
        baseHealthConfig.set("FIRE", 550);
        baseHealthConfig.set("DEATH", 650);
        baseHealthConfig.set("ICE", 800);
        baseHealthConfig.set("STORM", 400);
        baseHealthConfig.set("BALANCE", 650);
        baseHealthConfig.set("LIFE", 700);

        try{
            baseHealthConfig.save(baseHealthFile);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
