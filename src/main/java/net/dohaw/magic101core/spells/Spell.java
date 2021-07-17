package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Profile;
import net.dohaw.magic101core.profiles.Schools;
import net.dohaw.magic101core.runnables.AddBuffRunnable;
import net.dohaw.magic101core.runnables.LifeBubbleRunnable;
import net.dohaw.magic101core.runnables.RemoveBuffRunnable;
import net.dohaw.magic101core.utils.ALL_PROFILES;
import net.dohaw.magic101core.utils.PlayerLocationHelper;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.List;

public abstract class Spell {

    protected Location location;
    protected Player player;
    protected JavaPlugin plugin;

    public Spell(Location location, Player player, JavaPlugin plugin) {
        this.location = location;
        this.player = player;
        this.plugin = plugin;
    }

    public abstract void cast();

    protected void create15BlockRadius(Color color){
        for (int i = 0; i < 2 * Math.PI; i += (Math.PI/3.0)) {
            int y = location.getBlockY();
            int x = (int) (location.getBlockX() + Math.sin(i) * 15);
            int z = (int) (location.getBlockZ() + Math.cos(i) * 15);
            Location loc = new Location(location.getWorld(), x, y, z);
            firework(loc, color);
        }
    }

    private void firework(Location location, Color color) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withColor(color);
        meta.addEffect(builder.build());
        meta.setPower(1);
        firework.setFireworkMeta(meta);
    }

    //all spells that give temp buffs will use this function
    //duration is in seconds
    protected void applyBuff(String buff, double amount, int duration){
        List<Player> playersToBuff = PlayerLocationHelper.getAllPlayersNBlocksFromLocation(location,15);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        for(Player player: playersToBuff){
            player.sendMessage("A nearby spell has increased your " + buff + " rating by " + decimalFormat.format(amount*100) + "%");
            new AddBuffRunnable(player, buff, amount).runTask(plugin);
            new RemoveBuffRunnable(player, buff, amount).runTaskLater(plugin, 20*duration);
        }
    }
}
