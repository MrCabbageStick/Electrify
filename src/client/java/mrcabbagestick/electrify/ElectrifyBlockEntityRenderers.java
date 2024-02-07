package mrcabbagestick.electrify;

import mrcabbagestick.electrify.content.insulators.renderers.LargeInsulatorBlockEntityRenderer;
import mrcabbagestick.electrify.content.insulators.renderers.RollerInsulatorBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ElectrifyBlockEntityRenderers {

    public static void registerElectrifyBlockEntityRenderers(){
        BlockEntityRendererFactories.register(ElectrifyBlockEntities.ROLLER_INSULATOR_BLOCK_ENTITY, RollerInsulatorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ElectrifyBlockEntities.LARGE_INSULATOR_BLOCK_ENTITY, LargeInsulatorBlockEntityRenderer::new);
    }
}
