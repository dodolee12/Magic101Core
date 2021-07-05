package net.dohaw.magic101core.menus;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CreateItemMenu extends Menu implements Listener {

    public CreateItemMenu(JavaPlugin plugin) {
        super(plugin, null, "Create Item", 54);
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player p) {

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

    }
}
