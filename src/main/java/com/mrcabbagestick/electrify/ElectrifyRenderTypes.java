package com.mrcabbagestick.electrify;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class ElectrifyRenderTypes extends RenderStateShard {

	public static final RenderType WIRE = RenderType.create(
			"wire",
			DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
			VertexFormat.Mode.QUADS,
			256,
			RenderType.CompositeState.builder()
					.setShaderState(RENDERTYPE_LEASH_SHADER)
					.setTextureState(NO_TEXTURE)
					.setCullState(NO_CULL)
					.setLightmapState(LIGHTMAP)
					.createCompositeState(false)
	);
	public ElectrifyRenderTypes(String name, Runnable setupState, Runnable clearState) {
		super(name, setupState, clearState);
	}
}
