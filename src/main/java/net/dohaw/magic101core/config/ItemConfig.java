package net.dohaw.magic101core.config;

import net.dohaw.magic101core.items.CustomItem;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ItemConfig {
    private File itemFolder;
    private File tempFolder;
    private JavaPlugin plugin;

    public ItemConfig(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void valdiateConfig(){
        itemFolder = new File(plugin.getDataFolder(),"items/");
        tempFolder = new File(plugin.getDataFolder(),"tempitems/");
        if(!itemFolder.exists()){
            itemFolder.mkdirs();
        }
        if(!tempFolder.exists()){
            tempFolder.mkdirs();
        }
    }

    public void saveConfig(){
        deleteDirectoryFully(tempFolder);
        tempFolder.mkdirs();
        for(String key: ALL_ITEMS.ALL_ITEMS_MAP.keySet()){

            File itemFile = new File(tempFolder,key + ".yml");
            itemFile.delete();
            try{
                itemFile.createNewFile();
            }catch (Exception e) {
                e.printStackTrace();
            }
            FileConfiguration itemConfig = new YamlConfiguration();
            try {
                itemConfig.load(itemFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            itemConfig.set("item", ALL_ITEMS.ALL_ITEMS_MAP.get(key).toItemStack());

            try{
                itemConfig.save(itemFile);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        deleteDirectoryFully(itemFolder);
        tempFolder.renameTo(itemFolder);
    }

    public void loadConfig(){
        for(File itemFile: itemFolder.listFiles()){
            FileConfiguration itemConfig = new YamlConfiguration();
            try {
                itemConfig.load(itemFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            ItemStack itemStack = itemConfig.getItemStack("item");
            CustomItem customItem = new CustomItem(itemStack);
            ALL_ITEMS.ALL_ITEMS_MAP.put(customItem.getKEY(), customItem);
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
