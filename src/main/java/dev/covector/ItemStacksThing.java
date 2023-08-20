package dev.covector.mobarenacustomgive;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import com.garbagemule.MobArena.things.Thing;

public class ItemStacksThing implements Thing {

    private final List<ItemStack> stacks;

    public ItemStacksThing(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public boolean giveTo(Player player) {
        if (stacks == null) return false;
        return stacks.stream().allMatch(stack -> giveTo(player, stack));
    }

    public boolean giveTo(Player player, ItemStack stack) {
        return player.getInventory().addItem(stack.clone()).isEmpty();
    }

    @Override
    public boolean takeFrom(Player player) {
        if (stacks == null) return false;
        return stacks.stream().allMatch(stack -> takeFrom(player, stack));
    }

    public boolean takeFrom(Player player, ItemStack stack) {
        return player.getInventory().removeItem(stack.clone()).isEmpty();
    }

    @Override
    public boolean heldBy(Player player) {
        if (stacks == null) return false;
        return stacks.stream().anyMatch(stack -> heldBy(player, stack));
    }

    public boolean heldBy(Player player, ItemStack stack) {
        return player.getInventory().containsAtLeast(stack, stack.getAmount());
    }

    List<ItemStack> getItemStacks() {
        return stacks;
    }

    @Override
    public String toString() {
        String result = "";
        for (ItemStack stack : stacks) {
            result += stack.getAmount() + "x " + getName(stack) + ", ";
        }
        return result;
    }

    private String getName(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return stack.getType()
            .name()
            .replace("_", " ")
            .toLowerCase();
    }

}