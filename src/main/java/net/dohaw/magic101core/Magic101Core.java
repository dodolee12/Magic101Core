package net.dohaw.magic101core;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import net.dohaw.magic101core.commands.Magic101Command;
import net.dohaw.magic101core.commands.ProfileSelectCommand;
import net.dohaw.magic101core.config.ItemConfig;
import net.dohaw.magic101core.config.ProfileConfig;
import net.dohaw.magic101core.utils.ALL_ITEMS;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player player: Bukkit.getOnlinePlayers()){
                Inventory inventory = player.getInventory();
                for(ItemStack itemStack: inventory.getContents()){
                    if(itemStack == null){
                        continue;
                    }
                    ItemMeta meta = itemStack.getItemMeta();
                    if(meta == null){
                        continue;
                    }
                    if(meta.getPersistentDataContainer().has(NamespacedKey.minecraft("key"), PersistentDataType.STRING)){
                        inventory.remove(itemStack);
                        String key = meta.getPersistentDataContainer().get(NamespacedKey.minecraft("key"), PersistentDataType.STRING);
                        if(!ALL_ITEMS.ALL_ITEMS_MAP.containsKey(key)){
                            continue;
                        }
                        inventory.addItem(ALL_ITEMS.ALL_ITEMS_MAP.get(key).toItemStack());
                    }
                }
            }
        }, 5, 5*20);
    }

    private void kickAllPlayers(){
        for(Player player: Bukkit.getOnlinePlayers()){
            player.kickPlayer("Server Restart");
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
