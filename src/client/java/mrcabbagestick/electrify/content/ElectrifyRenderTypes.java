package mrcabbagestick.electrify.content;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class ElectrifyRenderTypes extends RenderLayer {

    public static final RenderLayer WIRE = RenderLayer.of(
            "wire",
            VertexFormats.POSITION_COLOR_LIGHT,
            VertexFormat.DrawMode.QUADS,
            256,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(RenderPhase.LEASH_PROGRAM)
                    .texture(RenderPhase.NO_TEXTURE)
                    .cull(RenderPhase.DISABLE_CULLING)
                    .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                    .build(false)
    );

    public ElectrifyRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}

