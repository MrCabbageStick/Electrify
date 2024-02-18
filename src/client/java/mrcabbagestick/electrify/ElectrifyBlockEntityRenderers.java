package mrcabbagestick.electrify;

import mrcabbagestick.electrify.content.wire_connectors.insulators.large_insulator.LargeInsulatorBlockEntityRenderer;
import mrcabbagestick.electrify.content.wire_connectors.insulators.roller_insulator.RollerInsulatorBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ElectrifyBlockEntityRenderers {

    public static void registerElectrifyBlockEntityRenderers(){
        BlockEntityRendererFactories.register(ElectrifyBlockEntities.ROLLER_INSULATOR_BLOCK_ENTITY, RollerInsulatorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ElectrifyBlockEntities.LARGE_INSULATOR_BLOCK_ENTITY, LargeInsulatorBlockEntityRenderer::new);
    }
}
