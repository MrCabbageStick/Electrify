package mrcabbagestick.electrify;

import mrcabbagestick.electrify.content.insulators.large_insulator.LargeInsulatorBlockEntity;
import mrcabbagestick.electrify.content.insulators.roller_insulator.RollerInsulatorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ElectrifyBlockEntities {

    public static final BlockEntityType<RollerInsulatorBlockEntity> ROLLER_INSULATOR_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Electrify.createIdentifier("roller_insulator_block_entity"),
            FabricBlockEntityTypeBuilder.create(RollerInsulatorBlockEntity::new, ElectrifyBlocks.ROLLER_INSULATOR).build()
    );

    public static final BlockEntityType<LargeInsulatorBlockEntity> LARGE_INSULATOR_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Electrify.createIdentifier("large_insulator_block_entity"),
            FabricBlockEntityTypeBuilder.create(LargeInsulatorBlockEntity::new, ElectrifyBlocks.LARGE_INSULATOR).build()
    );
    public static void registerElectrifyBlockEntities(){}
}
