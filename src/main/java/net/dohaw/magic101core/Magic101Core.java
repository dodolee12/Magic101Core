package net.dohaw.magic101core;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public final class Magic101Core extends JavaPlugin {

    private File profileFolder;

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);
        JPUtils.registerEvents(new EventListener(this));
        validateConfigs();
        loadCustomConfigs();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveCustomConfigs();
    }

    private void validateConfigs(){
        validateProfiles();
    }

    private void validateProfiles(){
        profileFolder = new File(getDataFolder(),"profiles/");
        if(!profileFolder.exists()){
            profileFolder.mkdirs();
        }
    }

    private void loadCustomConfigs(){
        loadProfileConfig();
    }

    private void loadProfileConfig(){
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
                Schools school = Schools.valueOf(profileConfig.getString("school-name"));
                int level = profileConfig.getInt("level");
                int maxHealth = profileConfig.getInt("max-health");
                int currentHealth = profileConfig.getInt("current-health");
                Location logoutLocation = profileConfig.getLocation("logout-location");
                OfflinePlayer player = profileConfig.getOfflinePlayer("offline-player");
                Profile createdProfile = Profile.loadProfileFromConfig(profileName,characterName,
                        school,level,maxHealth,currentHealth,logoutLocation);
                UUID playerUUID = player.getUniqueId();
                if(!ALL_PROFILES.ALL_PROFILES_MAP.containsKey(playerUUID)){
                    ALL_PROFILES.ALL_PROFILES_MAP.put(playerUUID, new ArrayList<>());
                }
                ALL_PROFILES.ALL_PROFILES_MAP.get(playerUUID).add(createdProfile);
            }
        }
    }

    private void saveCustomConfigs(){
        saveProfileConfig();
    }

    private void saveProfileConfig(){
        deleteDirectoryFully(profileFolder);
        profileFolder.mkdirs();
        for(UUID playerUUID: ALL_PROFILES.ALL_PROFILES_MAP.keySet()){
            File playerFolder = new File(profileFolder,playerUUID.toString() + "/");

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
                profileConfig.set("school-name", profile.getSchool().toString());
                profileConfig.set("level", profile.getLevel());
                profileConfig.set("max-health", profile.getHealth().getMaxHealth());
                profileConfig.set("current-health", profile.getHealth().getCurrentHealth());
                profileConfig.set("logout-location", profile.getLogoutLocation());
                profileConfig.set("offline-player", Bukkit.getOfflinePlayer(playerUUID));
                try{
                    profileConfig.save(profileFile);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
