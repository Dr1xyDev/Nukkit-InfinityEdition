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
import cn.nukkit.Server;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * NUKKIT INFINITY: Simplified transaction group.
 *
 */
public class SimpleTransactionGroup implements TransactionGroup {

    private final long creationTime;
    protected boolean hasExecuted = false;

    protected Player source = null;

    protected final Set<Inventory> inventories = new LinkedHashSet<>();

    // LinkedHashSet preserves arrival order — critical for correctness.
    protected final Set<Transaction> transactions = new LinkedHashSet<>();

    public SimpleTransactionGroup() {
        this(null);
    }

    public SimpleTransactionGroup(Player source) {
        this.creationTime = System.currentTimeMillis();
        this.source = source;
    }

    public Player getSource() {
        return source;
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public Set<Inventory> getInventories() {
        return inventories;
    }

    @Override
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (this.transactions.contains(transaction)) {
            return;
        }

        // Arrival-order replacement: last write to a slot wins.
        // Old Nukkit compared timestamps which could leave stale txns in the set.
        for (Transaction tx : new ArrayList<>(this.transactions)) {
            if (tx.getInventory().equals(transaction.getInventory()) && tx.getSlot() == transaction.getSlot()) {
                this.transactions.remove(tx);
            }
        }

        this.transactions.add(transaction);
        this.inventories.add(transaction.getInventory());
    }

    @Override
    public boolean canExecute() {
        // We can always execute — each transaction is independent.
        return !this.transactions.isEmpty();
    }

    @Override
    public boolean execute() {
        if (this.hasExecuted || !this.canExecute()) {
            return false;
        }

        InventoryTransactionEvent ev = new InventoryTransactionEvent(this);
        Server.getInstance().getPluginManager().callEvent(ev);

        if (ev.isCancelled()) {
            // Resync everything to the player.
            for (Inventory inventory : this.inventories) {
                if (inventory instanceof PlayerInventory) {
                    ((PlayerInventory) inventory).sendArmorContents(this.getSource());
                }
                inventory.sendContents(this.getSource());
            }
            return false;
        }

        // Apply each transaction independently in arrival order.
        for (Transaction transaction : new ArrayList<>(this.transactions)) {
            Item sourceItem = transaction.getSourceItem();
            Item targetItem = transaction.getTargetItem();

            // No-op: source and target are the same item with the same count.
            if (sourceItem.equals(targetItem, true, true)
                    && sourceItem.getCount() == targetItem.getCount()) {
                continue;
            }

            InventoryClickEvent event = new InventoryClickEvent(
                    transaction.getInventory(), this.getSource(),
                    transaction.getSlot(), sourceItem);
            Server.getInstance().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                transaction.getInventory().sendSlot(transaction.getSlot(), this.getSource());
                continue;
            }

            // Apply the slot change. setItem handles armor vs. main inv internally
            // (PlayerInventory.setItem fires EntityArmorChangeEvent for armor slots).
            transaction.getInventory().setItem(transaction.getSlot(), targetItem);
        }

        this.hasExecuted = true;

        // Final resync — kills any client-side ghost items.
        for (Inventory inventory : this.inventories) {
            if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).sendArmorContents(this.getSource());
            }
            inventory.sendContents(this.getSource());
        }

        return true;
    }

    @Override
    public boolean hasExecuted() {
        return this.hasExecuted;
    }
}
