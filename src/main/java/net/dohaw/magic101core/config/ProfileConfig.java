package net.dohaw.magic101core.config;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ProfileConfig {

    private File profileFolder;
    private File tempFolder;
    private JavaPlugin plugin;

    public ProfileConfig(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void valdiateConfig(){
        profileFolder = new File(plugin.getDataFolder(),"profiles/");
        tempFolder = new File(plugin.getDataFolder(),"tempprofiles/");
        if(!profileFolder.exists()){
            profileFolder.mkdirs();
        }
        if(!tempFolder.exists()){
            tempFolder.mkdirs();
        }
    }

    public void loadConfig(){
        for(File playerFiles: profileFolder.listFiles()){
            for(File profileFile: playerFiles.listFiles()){
                FileConfiguration profileConfig = new YamlConfiguration();
                try {
                    profileConfig.load(profileFile);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                String profileName = profileConfig.getString("profile-name");
                String characterName = profileConfig.getString("character-name");
                Schools school = null;
                if(profileConfig.getString("school-name") != null){
                    school = Schools.valueOf(profileConfig.getString("school-name"));

                }
                int level = profileConfig.getInt("level");
                int maxHealth = profileConfig.getInt("max-health");
                int currentHealth = profileConfig.getInt("current-health");
                Location logoutLocation = profileConfig.getLocation("logout-location");

                OfflinePlayer player = profileConfig.getOfflinePlayer("offline-player");
                ConfigurationSection armorConfigItems = profileConfig.getConfigurationSection("armor");
                ItemStack[] equippedItems = null;
                if(armorConfigItems != null){
                    equippedItems = new ItemStack[armorConfigItems.getKeys(false).size()];
                    int i = 0;
                    for(String key: armorConfigItems.getKeys(false)){
                        equippedItems[i++] = armorConfigItems.getItemStack(key);
                    }
                }

                ItemStack[] storageItems = null;
                ConfigurationSection storageConfigItems = profileConfig.getConfigurationSection("storage");
                if(storageConfigItems != null){
                    storageItems = new ItemStack[storageConfigItems.getKeys(false).size()];
                    int i = 0;
                    for(String key: storageConfigItems.getKeys(false)){
                        storageItems[i++] = storageConfigItems.getItemStack(key);
                    }
                }

                ItemStack[] extraItems = null;
                ConfigurationSection extraConfigItems = profileConfig.getConfigurationSection("extra");
                if(extraConfigItems != null){
                    extraItems = new ItemStack[extraConfigItems.getKeys(false).size()];
                    int i = 0;
                    for(String key: extraConfigItems.getKeys(false)){
                        extraItems[i++] = extraConfigItems.getItemStack(key);
                    }
                }


                Profile createdProfile = Profile.loadProfileFromConfig(profileName,characterName,
                        school,level,maxHealth,currentHealth,logoutLocation, equippedItems, storageItems, extraItems);
                UUID playerUUID = player.getUniqueId();
                if(!ALL_PROFILES.ALL_PROFILES_MAP.containsKey(playerUUID)){
                    ALL_PROFILES.ALL_PROFILES_MAP.put(playerUUID, new ArrayList<>());
                }
                ALL_PROFILES.ALL_PROFILES_MAP.get(playerUUID).add(createdProfile);
            }
        }
    }

    public void saveConfig(){
        deleteDirectoryFully(tempFolder);
        tempFolder.mkdirs();
        for(UUID playerUUID: ALL_PROFILES.ALL_PROFILES_MAP.keySet()){
            File playerFolder = new File(tempFolder,playerUUID.toString() + "/");

            playerFolder.mkdirs();

            for(Profile profile: ALL_PROFILES.ALL_PROFILES_MAP.get(playerUUID)){
                File profileFile = new File(playerFolder,profile.getProfileName() + ".yml");
                profileFile.delete();
                try{
                    profileFile.createNewFile();
                }catch (Exception e) {
                    e.printStackTrace();
                }
                FileConfiguration profileConfig = new YamlConfiguration();
                try {
                    profileConfig.load(profileFile);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                profileConfig.set("profile-name", profile.getProfileName());
                profileConfig.set("character-name", profile.getCharacterName());
                profileConfig.set("school-name", profile.getSchool().name());
                profileConfig.set("level", profile.getLevel());
                profileConfig.set("max-health", profile.getHealth().getMaxHealth());
                profileConfig.set("current-health", profile.getHealth().getCurrentHealth());
                profileConfig.set("logout-location", profile.getLogoutLocation());
                profileConfig.set("offline-player", Bukkit.getOfflinePlayer(playerUUID));

                if(profile.getEquippedArmor() != null){
                    for(int i = 0; i < profile.getEquippedArmor().length; ++i){
                        profileConfig.set("armor." + i,profile.getEquippedArmor()[i]);
                    }
                }

                if(profile.getStorageItems() != null){
                    for(int i = 0; i < profile.getStorageItems().length; ++i){
                        profileConfig.set("storage." + i, profile.getStorageItems()[i]);
                    }
                }

                if(profile.getExtraItems() != null){
                    for(int i = 0; i < profile.getExtraItems().length; ++i){
                        profileConfig.set("extra." + i, profile.getExtraItems()[i]);
                    }
                }

                try{
                    profileConfig.save(profileFile);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        deleteDirectoryFully(profileFolder);
        tempFolder.renameTo(profileFolder);
    }

    private boolean deleteDirectoryFully(File dir){
        if(dir.exists()){
            for(File file: dir.listFiles()){
                if (file.isDirectory()) {
                    deleteDirectoryFully(file);
                }
                else{
                    file.delete();
                }
            }
        }
        return dir.delete();
    }

}
