package net.dohaw.magic101core.config;

import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.utils.Constants;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MaterialConfig {

    private File configFolder;
    private File materialFile;

    private JavaPlugin plugin;

    public MaterialConfig(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void valdiateConfig(){
        configFolder = new File(plugin.getDataFolder(),"config/");
        if(!configFolder.exists()){
            configFolder.mkdirs();
        }
        materialFile = new File(configFolder,"materials.yml");
        if(!materialFile.exists()){
            createDefault();
        }

    }

    public void loadConfig(){
        FileConfiguration materialConfig = new YamlConfiguration();
        try {
            materialConfig.load(materialFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for(String key: materialConfig.getKeys(false)){
            Schools school = Schools.valueOf(key);
            Material material = Material.valueOf(materialConfig.getString(key));
            Constants.schoolsToMaterial.put(school,material);
        }

    }

    private void createDefault(){
        FileConfiguration materialConfig = new YamlConfiguration();

        try{
            materialFile.createNewFile();
            materialConfig.load(materialFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        materialConfig.set("MYTH", "STICK");
        materialConfig.set("FIRE", "FIRE_CHARGE");
        materialConfig.set("DEATH", "WITHER_SKELETON_SKULL");
        materialConfig.set("ICE", "ICE");
        materialConfig.set("STORM", "STICK");
        materialConfig.set("BALANCE", "STICK");
        materialConfig.set("LIFE", "GRASS");
        materialConfig.set("UNIVERSAL", "STICK");

        try{
            materialConfig.save(materialFile);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


}