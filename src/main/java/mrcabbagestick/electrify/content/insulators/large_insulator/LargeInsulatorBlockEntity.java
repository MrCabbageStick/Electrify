package mrcabbagestick.electrify.content.insulators.large_insulator;

import mrcabbagestick.electrify.ElectrifyBlockEntities;
import mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class LargeInsulatorBlockEntity extends WireConnectorBlockEntity {
    public LargeInsulatorBlockEntity(BlockPos pos, BlockState state) {
        super(ElectrifyBlockEntities.LARGE_INSULATOR_BLOCK_ENTITY, pos, state);
    }
}
