package net.dohaw.magic101core;

import net.dohaw.corelib.CoreLib;
import org.bukkit.plugin.java.JavaPlugin;

public final class Magic101Core extends JavaPlugin {

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
