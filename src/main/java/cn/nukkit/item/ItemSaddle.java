package cn.nukkit.item;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 * Reference: srcOfInfinity creative items
 */
public class ItemSaddle extends Item {

    public ItemSaddle() {
        this(0, 1);
    }

    public ItemSaddle(Integer meta) {
        this(meta, 1);
    }

    public ItemSaddle(Integer meta, int count) {
        super(SADDLE, meta, count, "Saddle");
    }
}
