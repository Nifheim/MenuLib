package net.nifheim.bukkit.menulib;

import org.bukkit.inventory.ItemStack;

/**
 * @author Beelzebu
 */
public interface MenuSlot {

    /**
     * Get the ItemStack for a menu slot.
     *
     * @return item.
     */
    ItemStack getItemStack();

    /**
     * Get the action for a menu slot.
     *
     * @return item.
     */
    Action getAction();

}
