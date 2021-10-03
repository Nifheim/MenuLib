package net.nifheim.bukkit.menulib;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Beelzebu
 */
public class Menu {

    private final Inventory inventory;
    private final Map<Integer, Action> actions = new HashMap<>();
    private final UUID uniqueId;

    protected Menu(String name, int size, Function<Integer, MenuSlot> slotProvider) {
        this(name, size, null, slotProvider);
    }

    protected Menu(String name, InventoryType type, Function<Integer, MenuSlot> slotProvider) {
        this(name, 0, type, slotProvider);
    }

    private Menu(String name, int size, InventoryType type, Function<Integer, MenuSlot> slotProvider) {
        if (type != null) {
            if (size < 9) {
                size *= 9;
            }
            if ((size % 9) > 0) {
                if (size > 53) {
                    size -= size - 53;
                } else {
                    size += 9 - (size % 9);
                }
            }
        }
        uniqueId = UUID.randomUUID();
        if (type != null && !type.equals(InventoryType.CHEST)) {
            inventory = Bukkit.createInventory(null, type, cleanTitle(name));
        } else {
            inventory = Bukkit.createInventory(null, size, cleanTitle(name));
        }
        if (slotProvider != null) {
            fill(slotProvider);
        }
    }

    protected void fill(Function<Integer, MenuSlot> slotProvider) {
        for (int i = 0; i < getInventory().getSize(); i++) {
            MenuSlot slot = slotProvider.apply(i);
            setItem(i, slot.getItemStack(), slot.getAction());
        }
    }

    public void setItem(int slot, ItemStack is, Action action) {
        inventory.setItem(slot, is);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    public void setItem(int slot, ItemStack is) {
        setItem(slot, is, null);
    }

    public void open(Player player) {
        UUID uniqueId = player.getUniqueId();
        UUID menuId = MenuLibrary.getInstance().getOpenMenus().get(uniqueId);
        if (menuId != null) {
            Logger.getLogger(Menu.class.getName()).log(Level.WARNING, "Can't open a new inventory for {0}, you must close the already open inventory first ({1})", new Object[]{player.getName(), menuId});
            return;
        }
        MenuLibrary.getInstance().addOpenMenu(player.getUniqueId(), getUniqueId());
        player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Map<Integer, Action> getActions() {
        return actions;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    private String cleanTitle(String title) {
        return ChatColor.translateAlternateColorCodes('&', title).replaceAll("[^\\w\\s&¿?()/\\-]", "");
    }
}
