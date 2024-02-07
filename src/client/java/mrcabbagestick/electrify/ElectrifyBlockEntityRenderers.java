package mrcabbagestick.electrify;

import mrcabbagestick.electrify.content.insulators.RollerInsulatorBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ElectrifyBlockEntityRenderers {

    public static void registerElectrifyBlockEntityRenderers(){
        BlockEntityRendererFactories.register(ElectrifyBlockEntities.ROLLER_INSULATOR_BLOCK_ENTITY, RollerInsulatorBlockEntityRenderer::new);
    }
}
