package net.dohaw.magic101core.spells;

import net.dohaw.corelib.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MinionSpell extends Spell{

    private final int MINIONS = 5;
    private final int DAMAGE = 10;

    public MinionSpell(Location location, Player player, JavaPlugin plugin) {
        super(location, player, plugin);
    }

    @Override
    public void cast() {
        List<Location> spawnLocations = getNMinionSpawnLocations(location,MINIONS);
        ItemStack minionWeapon = createMinionWeapon();
        for(Location spawnLocation: spawnLocations){
            LivingEntity minion = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
            minion.getEquipment().setItemInMainHand(minionWeapon);
            minion.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            minion.setCustomName(StringUtils.colorString("&eMinion"));
        }
    }

    private List<Location> getNMinionSpawnLocations(Location location, int n){
        List<Location> spawnLocations = new ArrayList<>();
        for(double angle = 0; angle < 2*Math.PI; angle += (Math.PI*2/n)){
            int newX = (int) (location.getBlockX() + Math.cos(angle)*4);
            int newY = location.getBlockY();
            int newZ = (int) (location.getBlockZ() + Math.sin(angle)*4);

            Location minionSpawnLocation = new Location(location.getWorld(),newX, newY, newZ);

            minionSpawnLocation.getBlock().breakNaturally();

            spawnLocations.add(minionSpawnLocation);
        }
        return spawnLocations;
    }

    private ItemStack createMinionWeapon(){
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(NamespacedKey.minecraft("damage"), PersistentDataType.INTEGER, DAMAGE);
        pdc.set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, player.getUniqueId().toString());
        item.setItemMeta(meta);
        return item;
    }
}
