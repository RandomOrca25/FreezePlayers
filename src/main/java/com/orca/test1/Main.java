package com.orca.test1;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("freezeplayer").setExecutor(new Freeze(this));
        getServer().getPluginManager().registerEvents(new FreezeListener(), this);
    }
}
