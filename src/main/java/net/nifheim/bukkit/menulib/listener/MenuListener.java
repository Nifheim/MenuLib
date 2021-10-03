package net.nifheim.bukkit.menulib.listener;

import java.util.UUID;
import net.nifheim.bukkit.menulib.Action;
import net.nifheim.bukkit.menulib.Menu;
import net.nifheim.bukkit.menulib.MenuLibrary;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Beelzebu
 */
public final class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        UUID inventoryUUID = MenuLibrary.getInstance().getOpenMenus().get(p.getUniqueId());
        if (inventoryUUID != null) {
            e.setCancelled(true);
            Menu gui = MenuLibrary.getInstance().getMenus().get(inventoryUUID);
            Action action = gui.getActions().get(e.getSlot());
            if (action != null) {
                action.click(p);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        MenuLibrary.getInstance().closeMenu(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        MenuLibrary.getInstance().closeMenu(e.getPlayer());
    }
}
