package net.dohaw.magic101core;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.menus.Menu;
import net.dohaw.magic101core.items.ItemProperties;
import net.dohaw.magic101core.menus.profile.ProfileSelectionMenu;
import net.dohaw.magic101core.menus.profile.ProfileViewMenu;
import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.spells.Spell;
import net.dohaw.magic101core.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {

    private JavaPlugin plugin;

    public EventListener(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.getInventory().clear();
        MenuUtil.openProfileSelectionMenu(plugin,player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        if(ALL_PROFILES.PROFILES_IN_SELECTION.contains(player.getUniqueId()) && ALL_PROFILES.findActiveProfile(player.getUniqueId()) == null){
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.sendMessage("You have tried to exit profile creation. You have been redirected to the profile selection screen.");
                ProfileSelectionMenu profileSelectionMenu = new ProfileSelectionMenu(plugin);
                profileSelectionMenu.initializeItems(player);
                profileSelectionMenu.openInventory(player);
            });
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Profile profile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
        if(profile == null){
            return;
        }
        profile.saveProfile(player);
        player.getInventory().clear();
    }

    @EventHandler
    public void gainXP(PlayerExpChangeEvent e){
        e.setAmount(0);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
        ItemStack itemInHand = e.getItem();

        if(itemInHand == null){
            return;
        }

        if(!itemIsUsable(activeProfile, itemInHand)){
            player.sendMessage("You do not meet the class requirement to use this item");
            e.setCancelled(true);
            return;
        }

        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){

            if(checkIfProfileChange(itemInHand)){
                Menu profileViewMenu = new ProfileViewMenu(plugin, activeProfile);
                profileViewMenu.initializeItems(player);
                profileViewMenu.openInventory(player);
                return;
            }

            PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(itemInHand);

            if(pdc == null){
                return;
            }

            String spellName = PropertyHelper.getStringFromPDC(pdc, "spell-name");

            if(spellName == null || spellName.equals("None")){
                return;
            }

            if(activeProfile.isOnCooldown()){
                player.sendMessage("Your " + spellName + " is currently on cooldown");
                return;
            }

            Location playerLocation = player.getLocation();

            ItemProperties aggregatedProperties = PropertyHelper.getAggregatedItemProperties(player);

            Spell spell = Constants.getSpellFromName(spellName, playerLocation, aggregatedProperties, player, plugin);

            cooldownHandler(activeProfile, player, spellName);

            spell.cast();

        }
    }

    private void cooldownHandler(Profile profile, Player player, String spellName){
        profile.setOnCooldown(true);
        Bukkit.getScheduler().runTaskLater(plugin, () ->{
            profile.setOnCooldown(false);
            player.sendMessage("Your " + spellName + " can now be used");
        },Constants.spellToCooldown.get(spellName)*20);
    }

    private boolean checkIfProfileChange(ItemStack itemInHand){
        if(!itemInHand.getType().equals(Material.BOOK)){
            return false;
        }
        if(!itemInHand.getItemMeta().getDisplayName().equals(StringUtils.colorString("&eRight Click to View Profile"))) {
            return false;
        }
        return true;
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        Entity attacker = e.getDamager();
        Entity attacked = e.getEntity();
        e.setDamage(0);

        if(attacker instanceof Player){
            Player attackerPlayer = (Player) attacker;
            Profile attackerProfile = ALL_PROFILES.findActiveProfile(attackerPlayer.getUniqueId());
            if(!itemIsUsable(attackerProfile, attackerPlayer.getInventory().getItemInMainHand())){
                e.setCancelled(true);
                return;
            }
        }
        if(attacker instanceof LivingEntity && attacked instanceof LivingEntity){
            DamageHelper.damageHandler((LivingEntity) attacker,(LivingEntity) attacked,plugin);
        }

    }

    @EventHandler
    public void onMythicMobSpawn(MythicMobSpawnEvent e){
        Entity mythicmob = e.getEntity();
        if(mythicmob instanceof LivingEntity){
            DisplayHealthUtil.updateHealth(mythicmob, (int) ((LivingEntity) mythicmob).getHealth());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        Entity mob = e.getEntity();
        if(mob instanceof LivingEntity){
            DisplayHealthUtil.updateHealth(mob, (int) ((LivingEntity) mob).getHealth());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
        if(activeProfile != null){
            activeProfile.getHealth().healToFull();
            activeProfile.saveProfile(player);

            player.getInventory().clear();
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            MenuUtil.openProfileSelectionMenu(plugin,player);
        },1);
    }


    private boolean itemIsUsable(Profile profile, ItemStack item){

        PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(item);

        if(pdc == null){
            return true;
        }

        String schoolName = PropertyHelper.getStringFromPDC(pdc,"school");

        if(schoolName == null){
            return true;
        }

        Schools school = Schools.valueOf(schoolName);

        if(school != Schools.UNIVERSAL && school != profile.getSchool()) {
            return false;
        }

        return true;
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e){
        if(e.getMethod().equals(ArmorEquipEvent.EquipMethod.DEATH)){
            return;
        }
        Player player = e.getPlayer();
        Profile activeProfile = ALL_PROFILES.findActiveProfile(player.getUniqueId());
        ItemStack newArmor = e.getNewArmorPiece();

        if(!itemIsUsable(activeProfile, newArmor)){
            player.sendMessage("You do not meet the class requirement to use this item");
            e.setCancelled(true);
            return;
        }

        //unequip
        if(newArmor == null){
            ItemStack oldArmor = e.getOldArmorPiece();

            PersistentDataContainer pdc = PropertyHelper.getPDCFromItem(oldArmor);

            int maxHealthLoss = -PropertyHelper.getIntegerFromPDC(pdc,"max-health");

            activeProfile.getHealth().changeMaxHealth(maxHealthLoss);

        }
        //equip
        else{
            ItemStack oldArmor = e.getOldArmorPiece();

            PersistentDataContainer oldArmorPDC = PropertyHelper.getPDCFromItem(oldArmor);

            PersistentDataContainer newArmorPDC = PropertyHelper.getPDCFromItem(newArmor);
            int maxHealthLoss = 0;
            int maxHealthGain = 0;
            if(oldArmorPDC != null){
                maxHealthLoss = PropertyHelper.getIntegerFromPDC(oldArmorPDC,"max-health");
            }
            if(newArmorPDC != null){
                maxHealthGain = PropertyHelper.getIntegerFromPDC(newArmorPDC, "max-health");

            }
            activeProfile.getHealth().changeMaxHealth(maxHealthGain - maxHealthLoss);
        }

        DisplayHealthUtil.updateHealth(activeProfile, player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        int slotClicked = e.getSlot();

        if(slotClicked == 8){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        if(checkIfProfileChange(item)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMinionTarget(EntityTargetEvent e){
        if(e.getTarget() instanceof Player && e.getEntity() instanceof LivingEntity){
            Player targeted = (Player) e.getTarget();
            LivingEntity attacker = (LivingEntity) e.getEntity();
            EntityEquipment equip = attacker.getEquipment();
            if(equip == null){
                return;
            }
            ItemMeta meta = equip.getItemInMainHand().getItemMeta();
            if(meta == null){
                return;
            }
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if(pdc.has(NamespacedKey.minecraft("owner"), PersistentDataType.STRING) &&
            pdc.get(NamespacedKey.minecraft("owner"), PersistentDataType.STRING).equals(targeted.getUniqueId().toString())){
                e.setCancelled(true);
            }

        }
    }

}
