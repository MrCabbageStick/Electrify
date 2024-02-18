package mrcabbagestick.electrify.content.wire_connectors.insulators.roller_insulator;

import mrcabbagestick.electrify.ElectrifyBlockEntities;
import mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class RollerInsulatorBlockEntity extends WireConnectorBlockEntity {
    public RollerInsulatorBlockEntity(BlockPos pos, BlockState state) {
        super(ElectrifyBlockEntities.ROLLER_INSULATOR_BLOCK_ENTITY, pos, state);
    }
}

