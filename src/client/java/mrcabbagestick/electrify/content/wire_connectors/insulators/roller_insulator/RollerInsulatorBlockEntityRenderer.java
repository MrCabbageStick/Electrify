package mrcabbagestick.electrify.content.wire_connectors.insulators.roller_insulator;

import mrcabbagestick.electrify.content.wire_connectors.insulators.roller_insulator.RollerInsulatorBlockEntity;
import mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class RollerInsulatorBlockEntityRenderer extends WireConnectorBlockEntityRenderer<RollerInsulatorBlockEntity> {

    public RollerInsulatorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
