package mrcabbagestick.electrify;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ElectrifyItems {

    public static final Item ROLLER_INSULATOR = registerBlockItem(ElectrifyBlocks.ROLLER_INSULATOR, "roller_insulator");
    public static final Item LARGE_INSULATOR = registerBlockItem(ElectrifyBlocks.LARGE_INSULATOR, "large_insulator");
    public static Item registerItem(Item item, String name){
        return Registry.register(Registries.ITEM, Electrify.createIdentifier(name), item);
    }
    public static Item registerBlockItem(Block block, String name){
        return registerItem(new BlockItem(block, new FabricItemSettings()), name);
    }

    public static void registerItems(){}
}
