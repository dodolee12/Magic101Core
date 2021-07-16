package net.dohaw.magic101core.spells;

import net.dohaw.magic101core.profiles.Schools;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public abstract class Spell {

    protected Location location;
    protected Player player;

    public Spell(Location location, Player player) {
        this.location = location;
        this.player = player;
    }

    public abstract void cast();

    public void create15BlockRadius(Color color){
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

}
