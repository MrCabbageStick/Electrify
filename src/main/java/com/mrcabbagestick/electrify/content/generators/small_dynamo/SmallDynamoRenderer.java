package com.mrcabbagestick.electrify.content.generators.small_dynamo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class SmallDynamoRenderer extends KineticBlockEntityRenderer<SmallDynamoBlockEntity> {
	public SmallDynamoRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(SmallDynamoBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);
	}

	@Override
	protected BlockState getRenderedBlockState(SmallDynamoBlockEntity be) {
		return shaft(getRotationAxisOf(be));
	}
}
