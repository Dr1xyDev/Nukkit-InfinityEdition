package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * Added for MCPE 0.15.10 creative inventory completeness
 */
public class BlockPistonSticky extends BlockSolid {

    public BlockPistonSticky() {
        this(0);
    }

    public BlockPistonSticky(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return STICKY_PISTON;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public String getName() {
        return "Sticky Piston";
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{
                    {Item.STICKY_PISTON, 0, 1}
            };
        } else {
            return new int[0][0];
        }
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.STONE_BLOCK_COLOR;
    }
}
