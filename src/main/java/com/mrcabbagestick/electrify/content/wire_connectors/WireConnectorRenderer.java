package com.mrcabbagestick.electrify.content.wire_connectors;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mrcabbagestick.electrify.ElectrifyRenderTypes;

import com.mrcabbagestick.electrify.content.wires.WireRenderFunctions;

import com.mrcabbagestick.electrify.content.wires.WireType;

import net.fabricmc.fabric.mixin.blockrenderlayer.RenderLayersMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import net.minecraft.world.level.block.state.BlockState;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WireConnectorRenderer<C extends WireConnectorBlockEntity> implements BlockEntityRenderer<C> {
	public WireConnectorRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(C blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();

		Matrix4f positionMatrix = poseStack.last().pose();
		VertexConsumer _buffer = buffer.getBuffer(ElectrifyRenderTypes.WIRE);

		BlockState startingBlock = Minecraft.getInstance().level.getBlockState(blockEntity.getBlockPos());

		Vector3f startPos = (startingBlock.getBlock() instanceof WireConnectorBaseBlock<?> startingConnector)
				? startingConnector.getConnectorOffset(startingBlock)
				: new Vector3f(.5f, .5f, .5f);

		for(Vector3f endPos : blockEntity.renderTo){

			WireRenderFunctions.renderWire(
					startPos,
					endPos,
					WireType.STEEL_WIRE,
					positionMatrix,
					_buffer,
					packedLight,
					packedOverlay
			);

		}

		poseStack.popPose();
	}
}
