package mrcabbagestick.electrify;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ElectrifyBlocks {

    public static final Block TEST_BLOCK = registerBlock(new Block(FabricBlockSettings.copyOf(Blocks.DIRT)), "test_block");
    public static Block registerBlock(Block block, String name){
        return Registry.register(Registries.BLOCK, Electrify.createIdentifier(name), block);
    }

    public static void registerBlocks(){}

}
