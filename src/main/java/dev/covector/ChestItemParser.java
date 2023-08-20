package dev.covector.mobarenacustomgive;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.World;

import java.util.List;
import java.util.ArrayList;

import com.garbagemule.MobArena.things.ThingParser;

public class ChestItemParser implements ThingParser {
    private Server server;

    public ChestItemParser(Server server) {
        this.server = server;
    }

    @Override
    public ItemStacksThing parse(String itemKey) {
        List<ItemStack> stacks = parseItemStacks(itemKey);
        if (stacks == null) return null;
        return new ItemStacksThing(stacks);
    }

    public List<ItemStack> parseItemStacks(String itemKey) {
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
        int x, y, z;
        try {
            x = Integer.parseInt(coords[0]);
            y = Integer.parseInt(coords[1]);
            z = Integer.parseInt(coords[2]);
        } catch (NumberFormatException e) {
            server.getLogger().severe(String.format(
                "Failed to parse '%s' - the format should be 'ci:<world-name>:<chest-x-coord> <chest-y-coord> <chest-z-coord>:<item-index>'",
            itemKey));
            return null;
        }
        int idStart, idEnd;
        if (args[3].contains("-")) {
            String[] idRange = args[3].split("-");
            if (idRange.length != 2) {
                server.getLogger().severe(String.format(
                    "Failed to parse '%s' - <item-index> should be a number or a range of numbers in the format '<start>-<end>'",
                args[3]));
                return null;
            }
            try {
                idStart = Integer.parseInt(idRange[0]);
                idEnd = Integer.parseInt(idRange[1]);
            } catch (NumberFormatException e) {
                server.getLogger().severe(String.format(
                    "Failed to parse '%s' - <item-index> should be a number or a range of numbers in the format '<start>-<end>'",
                args[3]));
                return null;
            }
        } else {
            try {
                idStart = Integer.parseInt(args[3]);
                idEnd = idStart;
            } catch (NumberFormatException e) {
                server.getLogger().severe(String.format(
                    "Failed to parse '%s' - the format should be 'ci:<world-name>:<chest-x-coord> <chest-y-coord> <chest-z-coord>:<item-index>'",
                itemKey));
                return null;
            }
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
            return null;
        }
        Chest chest = (Chest) block.getState();

        List<ItemStack> items = new ArrayList<ItemStack>();
        for (int i = idStart; i <= idEnd; i++) {
            ItemStack item = chest.getBlockInventory().getItem(i);
            if (item == null) {
                server.getLogger().info(String.format(
                    "There is no item at '%s'",
                i));
            } else {
                items.add(item.clone());
            }
        }
        return items;
    }
}