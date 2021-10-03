package net.nifheim.bukkit.menulib;

import org.bukkit.entity.Player;

/**
 * @author Beelzebu
 */
@FunctionalInterface
public interface Action {

    void click(Player p);
}
