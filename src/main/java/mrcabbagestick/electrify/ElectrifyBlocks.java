package mrcabbagestick.electrify;

import mrcabbagestick.electrify.content.insulators.RollerInsulator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ElectrifyBlocks {

    public static final Block ROLLER_INSULATOR = registerBlock(new RollerInsulator(FabricBlockSettings.copyOf(Blocks.LEVER)), "roller_insulator");
    public static Block registerBlock(Block block, String name){
        return Registry.register(Registries.BLOCK, Electrify.createIdentifier(name), block);
    }

    public static void registerBlocks(){}

}
