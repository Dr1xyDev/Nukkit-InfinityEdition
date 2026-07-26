package cn.nukkit.item;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 * Reference: srcOfInfinity creative items
 */
public class ItemNameTag extends Item {

    public ItemNameTag() {
        this(0, 1);
    }

    public ItemNameTag(Integer meta) {
        this(meta, 1);
    }

    public ItemNameTag(Integer meta, int count) {
        super(NAME_TAG, meta, count, "Name Tag");
    }
}
