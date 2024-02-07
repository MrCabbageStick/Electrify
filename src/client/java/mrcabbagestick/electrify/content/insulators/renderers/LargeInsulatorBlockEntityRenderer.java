package mrcabbagestick.electrify.content.insulators.renderers;

import mrcabbagestick.electrify.content.insulators.large_insulator.LargeInsulatorBlockEntity;
import mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class LargeInsulatorBlockEntityRenderer extends WireConnectorBlockEntityRenderer<LargeInsulatorBlockEntity> {
    public LargeInsulatorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
