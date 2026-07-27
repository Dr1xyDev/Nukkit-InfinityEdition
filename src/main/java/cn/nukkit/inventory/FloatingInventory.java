/*
 *
 *
 * NUKKIT INFINITY
 *
 *
 * Mantenido por @Dr1xyDev
 * GH: https://www.github.com/Dr1xyDev/Nukkit-InfinityEdition
 *
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

import java.util.Collection;

/**
 * Virtual inventory that holds items "in transit" during transactions.
 * Ported from Infinity PMMP — used by the transaction queue to validate
 * equip / unequip / move operations atomically and prevent duplication.
 */
public class FloatingInventory extends BaseInventory {

    private static final int FI_SIZE = 36;

    public FloatingInventory(InventoryHolder holder) {
        // Use PLAYER type just so the BaseInventory constructor doesn't NPE;
        // we override everything that matters below.
        super(holder, InventoryType.get(InventoryType.PLAYER), new java.util.HashMap<>());
        this.size = FI_SIZE;
    }

    @Override
    public int getSize() {
        return FI_SIZE;
    }

    @Override
    public String getName() {
        return "Floating";
    }

    @Override
    public String getTitle() {
        return "Floating Inventory";
    }

    @Override
    public boolean setItem(int index, Item item) {
        // Bypass EntityInventoryChangeEvent — FloatingInventory is internal.
        if (index < 0 || index >= this.size) {
            return false;
        } else if (item.getId() == 0 || item.getCount() <= 0) {
            return this.clear(index);
        }
        this.slots.put(index, item.clone());
        return true;
    }

    @Override
    public boolean clear(int index) {
        this.slots.remove(index);
        return true;
    }

    @Override
    public void onSlotChange(int index, Item before) {
        // No viewers — silent.
    }

    @Override
    public void sendContents(Player player) { /* silent */ }
    @Override
    public void sendContents(Player[] players) { /* silent */ }
    @Override
    public void sendContents(Collection<Player> players) { /* silent */ }
    @Override
    public void sendSlot(int index, Player player) { /* silent */ }
    @Override
    public void sendSlot(int index, Player[] players) { /* silent */ }
    @Override
    public void sendSlot(int index, Collection<Player> players) { /* silent */ }
}
