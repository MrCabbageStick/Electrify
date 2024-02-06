package mrcabbagestick.electrify;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ElectrifyItems {

    public static final Item TEST_BLOCK_ITEM = registerBlockItem(ElectrifyBlocks.TEST_BLOCK, "test_block");
    public static Item registerItem(Item item, String name){
        return Registry.register(Registries.ITEM, Electrify.createIdentifier(name), item);
    }
    public static Item registerBlockItem(Block block, String name){
        return registerItem(new BlockItem(block, new FabricItemSettings()), name);
    }

    public static void registerItems(){}
}
