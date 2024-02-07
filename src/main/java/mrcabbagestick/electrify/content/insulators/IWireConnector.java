package mrcabbagestick.electrify.content.insulators;

import net.minecraft.block.BlockState;
import org.joml.Vector3f;

public interface IWireConnector {
    default Vector3f getConnectorOffset(BlockState state){
        return new Vector3f(0, 0, 0);
    }
}
