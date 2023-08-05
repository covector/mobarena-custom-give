package dev.covector.mobarenacustomgive;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.World;
import com.garbagemule.MobArena.things.ItemStackParser;

public class ChestItemParser implements ItemStackParser {
    private Server server;

    public ChestItemParser(Server server) {
        this.server = server;
    }

    @Override
    @Nullable
    public ItemStack parse(String itemKey) {
        if (!itemKey.startsWith("ci:")) return null;
        String[] args = itemKey.split(":");
        if (args.length != 4) {
            server.getLogger().warning(String.format(
                "Failed to parse '%s' - the format should be 'ci:<world-name>:<chest-x-coord> <chest-y-coord> <chest-z-coord>:<item-index>'",
            itemKey));
            return null;
        }
        String[] coords = args[2].split(" ");
        if (coords.length != 3) {
            server.getLogger().warning(String.format(
                "Failed to parse coordinates '%s' - the format should be '<chest-x-coord> <chest-y-coord> <chest-z-coord>'",
            args[2]));
            return null;
        }
        int x, y, z, index;
        try {
            x = Integer.parseInt(coords[0]);
            y = Integer.parseInt(coords[1]);
            z = Integer.parseInt(coords[2]);
            index = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            server.getLogger().severe(String.format(
                "Failed to parse '%s' - the format should be 'ci:<world-name>:<chest-x-coord> <chest-y-coord> <chest-z-coord>:<item-index>'",
            itemKey));
            return null;
        }
        World world = server.getWorld(args[1]);
        if (world == null) {
            server.getLogger().severe(String.format(
                "The world '%s' does not exist",
            args[1]));
            return null;
        }
        Block block = world.getBlockAt(x, y, z);
        if (block.getType() != Material.CHEST) {
            server.getLogger().severe(String.format(
                "The block at '%s' is not a chest",
            args[2]));
            return new ItemStack(Material.AIR);
        }
        Chest chest = (Chest) block.getState();
        ItemStack items = chest.getBlockInventory().getItem(index);
        if (items == null) {
            server.getLogger().severe(String.format(
                "There is no items at '%s'",
            args[3]));
            return new ItemStack(Material.AIR);
        }
        return items.clone();
    }
}