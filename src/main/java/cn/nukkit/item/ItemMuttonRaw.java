package cn.nukkit.item;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 * Reference: srcOfInfinity creative items
 */
public class ItemMuttonRaw extends ItemEdible {

    public ItemMuttonRaw() {
        this(0, 1);
    }

    public ItemMuttonRaw(Integer meta) {
        this(meta, 1);
    }

    public ItemMuttonRaw(Integer meta, int count) {
        super(RAW_MUTTON, meta, count, "Raw Mutton");
    }
}
