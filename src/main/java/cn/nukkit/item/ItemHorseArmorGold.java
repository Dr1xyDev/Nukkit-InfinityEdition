package cn.nukkit.item;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 * Reference: srcOfInfinity creative items
 */
public class ItemHorseArmorGold extends Item {

    public ItemHorseArmorGold() {
        this(0, 1);
    }

    public ItemHorseArmorGold(Integer meta) {
        this(meta, 1);
    }

    public ItemHorseArmorGold(Integer meta, int count) {
        super(HORSE_ARMOR_GOLD, meta, count, "Gold Horse Armor");
    }
}
