package mrcabbagestick.electrify.content.wire_connectors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import org.joml.Vector3f;

public abstract class WireConnectorBaseBlock extends Block implements BlockEntityProvider{
    public WireConnectorBaseBlock(Settings settings) {
        super(settings);
    }

    public abstract Vector3f getConnectorOffset(BlockState state);

}
