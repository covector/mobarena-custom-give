package dev.covector.mobarenacustomgive;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import com.garbagemule.MobArena.things.ItemStackParser;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;

public class MMOItemParser implements ItemStackParser {
    private Server server;

    public MMOItemParser(Server server) {
        this.server = server;
    }

    @Override
    @Nullable
    public ItemStack parse(String itemKey) {
        if (!itemKey.startsWith("mi:")) return null;
        String[] args = itemKey.split(":");
        if (args.length != 3 && args.length != 5) {
            server.getLogger().severe(String.format(
                "Failed to parse '%s' - the format should be 'mi:<type>:<id>' or 'mi:<type>:<id>:<level>:<tier>'",
            itemKey));
            return null;
        }
        Type type = MMOItems.plugin.getTypes().get(args[1]);
        if (type == null) {
            server.getLogger().severe(String.format(
                "'%s' is not a valid type",
            args[1]));
            return null;
        }
        MMOItem mmoitem;
        if (args.length == 3) {
            mmoitem = MMOItems.plugin.getMMOItem(type, args[2]);
            if (mmoitem == null) {
                server.getLogger().severe(String.format(
                    "'%s' is not a valid id",
                args[2]));
                return null;
            }
        } else {
            int level;
            try {
                level = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                server.getLogger().severe(String.format(
                    "Failed to parse '%s' - the level should be an integer",
                args[3]));
                return null;
            }
            ItemTier tier = MMOItems.plugin.getTiers().get(args[4]);
            if (tier == null) {
                server.getLogger().severe(String.format(
                    "'%s' is not a valid tier",
                args[4]));
                return null;
            }
            mmoitem = MMOItems.plugin.getMMOItem(type, args[2], level, tier);
            if (mmoitem == null) {
                server.getLogger().severe(String.format(
                    "The id or level is invalid for '%s'",
                itemKey));
                return null;
            }
        }
        return mmoitem.newBuilder().build();
    }
}