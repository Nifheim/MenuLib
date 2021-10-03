package net.nifheim.bukkit.menulib;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import net.nifheim.bukkit.menulib.listener.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

/**
 * @author Beelzebu
 */
public final class MenuLibrary {

    private static MenuLibrary instance;

    private final Map<UUID, Menu> menus = new HashMap<>();
    private final Map<UUID, UUID> openMenus = new HashMap<>();

    public MenuLibrary(Plugin plugin) {
        if (instance != null) {
            throw new IllegalStateException("A library instance already exists");
        }
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
        instance = this;
    }

    public static MenuLibrary getInstance() {
        if (instance == null) {
            throw new RuntimeException("Library instance not created yet");
        }
        return instance;
    }

    public Menu register(String name, int size, Function<Integer, MenuSlot> slotProvider) {
        Menu menu = new Menu(name, size, slotProvider);
        menus.put(menu.getUniqueId(), menu);
        return menu;
    }

    public Menu register(String name, InventoryType inventoryType, Function<Integer, MenuSlot> slotProvider) {
        Menu menu = new Menu(name, inventoryType, slotProvider);
        menus.put(menu.getUniqueId(), menu);
        return menu;
    }

    void addOpenMenu(UUID player, UUID menu) {
        synchronized (openMenus) {
            openMenus.put(player, menu);
        }
    }

    public void closeMenu(HumanEntity player) {
        synchronized (openMenus) {
            player.closeInventory();
            openMenus.remove(player.getUniqueId());
        }
    }

    public void closeMenu(UUID playerId) {
        synchronized (openMenus) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                closeMenu(player);
            } else {
                openMenus.remove(playerId);
            }
        }
    }

    public void unregisterMenu(Menu menu) {
        menus.remove(menu.getUniqueId());
    }

    public void unregisterMenu(UUID menuId) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID openMenuId = MenuLibrary.getInstance().getOpenMenus().get(player.getUniqueId());
            if (openMenuId.equals(menuId)) {
                player.closeInventory();
            }
        });
        menus.remove(menuId);
    }

    /**
     * Get an immutable map of all created menus with his id as key.
     * Note that the same menu can be opened by multiple players.
     *
     * @return immutable map containing all created menus.
     */
    public Map<UUID, Menu> getMenus() {
        return ImmutableMap.copyOf(menus);
    }

    /**
     * Get an immutable map of all open inventories with the player's uuid as key.
     * Note that a player can only open a single menu at the same time.
     *
     * @return immutable map containing all open menus.
     */
    public Map<UUID, UUID> getOpenMenus() {
        return ImmutableMap.copyOf(openMenus);
    }

    public static Menu openMenu(Player player, Menu menu) {
        player.openInventory(menu.getInventory());
        return menu;
    }
}
