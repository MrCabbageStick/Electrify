package com.mrcabbagestick.electrify.content.wire_connectors;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.vertex.VertexConsumer;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.ElectrifyRenderTypes;

import com.mrcabbagestick.electrify.content.wires.WireRenderFunctions;

import com.mrcabbagestick.electrify.content.wires.WireType;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;

import net.fabricmc.fabric.mixin.blockrenderlayer.RenderLayersMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.Vec3;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WireConnectorRenderer<C extends WireConnectorBlockEntity> extends SmartBlockEntityRenderer<C> {
	public WireConnectorRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void renderSafe(C blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();

//		Electrify.LOGGER.info("Rendering");

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
	@Override
	public boolean shouldRenderOffScreen(C blockEntity) {
		return true;
	}
}
