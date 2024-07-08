package com.mrcabbagestick.electrify.content.wire_connectors;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcabbagestick.electrify.ElectrifyRenderTypes;
import com.mrcabbagestick.electrify.content.wires.WireRenderFunctions;
import com.mrcabbagestick.electrify.content.wires.WireType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class WireConnectorRenderer<C extends WireConnectorBlockEntity> implements BlockEntityRenderer<C> {

	public WireConnectorRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(@NotNull C blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

		poseStack.pushPose();

//		Electrify.LOGGER.info("Rendering");

		Matrix4f positionMatrix = poseStack.last().pose();
		VertexConsumer _buffer = buffer.getBuffer(ElectrifyRenderTypes.WIRE);

		if(Minecraft.getInstance().level == null)
			return;

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
	public boolean shouldRenderOffScreen(@NotNull C blockEntity) {

		return true;
	}
}
