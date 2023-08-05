package dev.covector.mobarenacustomgive;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.things.ThingManager;

public class CustomGivePlugin extends JavaPlugin
{
    @Override
    public void onEnable() {
        Plugin maplugin = getServer().getPluginManager().getPlugin("MobArena");
        if (maplugin == null) {
            getLogger().warning("MobArena Not Loaded!");
            return;
        }
        MobArena mobarena = (MobArena) maplugin;
        ThingManager thingman = mobarena.getThingManager();

        ChestItemParser chestItemParser = new ChestItemParser(Bukkit.getServer());
        thingman.register(chestItemParser);

        MMOItemParser mmoItemParser = new MMOItemParser(Bukkit.getServer());
        thingman.register(mmoItemParser);

        mobarena.reload();
        getLogger().info("MobArena Custom Give Plugin Activated!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MobArena Custom Give Plugin Deactivated!");
    }
}
