package cn.nukkit.item;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 * Reference: srcOfInfinity creative items
 */
public class ItemLead extends Item {

    public ItemLead() {
        this(0, 1);
    }

    public ItemLead(Integer meta) {
        this(meta, 1);
    }

    public ItemLead(Integer meta, int count) {
        super(LEAD, meta, count, "Lead");
    }
}
