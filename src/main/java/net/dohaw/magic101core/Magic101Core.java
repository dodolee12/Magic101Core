package net.dohaw.magic101core;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Magic101Core extends JavaPlugin {

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);
        JPUtils.registerEvents(new EventListener(this));
    }

    @Override
    public void onDisable() {





    }

}
