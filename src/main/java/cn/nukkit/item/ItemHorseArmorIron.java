package cn.nukkit.item;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 * Reference: srcOfInfinity creative items
 */
public class ItemHorseArmorIron extends Item {

    public ItemHorseArmorIron() {
        this(0, 1);
    }

    public ItemHorseArmorIron(Integer meta) {
        this(meta, 1);
    }

    public ItemHorseArmorIron(Integer meta, int count) {
        super(HORSE_ARMOR_IRON, meta, count, "Iron Horse Armor");
    }
}
