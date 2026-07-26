package cn.nukkit.item;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 * Reference: srcOfInfinity creative items
 */
public class ItemHorseArmorDiamond extends Item {

    public ItemHorseArmorDiamond() {
        this(0, 1);
    }

    public ItemHorseArmorDiamond(Integer meta) {
        this(meta, 1);
    }

    public ItemHorseArmorDiamond(Integer meta, int count) {
        super(HORSE_ARMOR_DIAMOND, meta, count, "Diamond Horse Armor");
    }
}
