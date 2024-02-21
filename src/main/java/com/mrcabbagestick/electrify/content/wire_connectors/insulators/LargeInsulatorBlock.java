package com.mrcabbagestick.electrify.content.wire_connectors.insulators;

import com.google.common.collect.ImmutableMap;
import com.mrcabbagestick.electrify.tools.RotationDegrees;
import com.mrcabbagestick.electrify.tools.VoxelShapeTools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.joml.Vector3f;

import com.mrcabbagestick.electrify.content.wire_connectors.FacingWireConnectorBlockBase;
import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import com.mrcabbagestick.electrify.entries.ElectrifyBlockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.function.Function;

public class LargeInsulatorBlock extends FacingWireConnectorBlockBase<WireConnectorBlockEntity> {

	public static final VoxelShape BASE_SHAPE;
	private static final VoxelShape HORIZONTAL_SHAPE;
	public static final Map<Direction, VoxelShape> SHAPE_MAP;
	public static final Map<Direction, Vector3f> CONNECTOR_OFFSET_MAP;
	public LargeInsulatorBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Vector3f getConnectorOffset(BlockState state) {
		return CONNECTOR_OFFSET_MAP.get(state.getValue(FACING));
	}

	@Override
	public Class<WireConnectorBlockEntity> getBlockEntityClass() {
		return WireConnectorBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends WireConnectorBlockEntity> getBlockEntityType() {
		return ElectrifyBlockEntities.WIRE_CONNECTOR_BLOCK_ENTITY.get();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_MAP.get(state.getValue(FACING));
	}

	static{
		BASE_SHAPE = Shapes.box(5f / 16f, 0f, 5f / 16f, 11f / 16f, 14.5f / 16f, 11f / 16f);
		HORIZONTAL_SHAPE = VoxelShapeTools.rotate(BASE_SHAPE, RotationDegrees.DEG90, Direction.Axis.X);
		SHAPE_MAP = Map.of(
				Direction.DOWN, BASE_SHAPE,
				Direction.UP, VoxelShapeTools.rotate(BASE_SHAPE, RotationDegrees.DEG180, Direction.Axis.X),

				Direction.NORTH, HORIZONTAL_SHAPE,
				Direction.EAST, VoxelShapeTools.rotate(HORIZONTAL_SHAPE, RotationDegrees.DEG90, Direction.Axis.Y),
				Direction.SOUTH, VoxelShapeTools.rotate(HORIZONTAL_SHAPE, RotationDegrees.DEG180, Direction.Axis.Y),
				Direction.WEST, VoxelShapeTools.rotate(HORIZONTAL_SHAPE, RotationDegrees.DEG270, Direction.Axis.Y)
		);

		CONNECTOR_OFFSET_MAP = Map.of(
				Direction.DOWN, new Vector3f(.5f, 14f / 16f, .5f),
				Direction.UP, new Vector3f(.5f, 2f / 16f, .5f),

				Direction.NORTH, new Vector3f(.5f, .5f, 14f / 16f),
				Direction.EAST, new Vector3f(2f / 16f, .5f, .5f),
				Direction.SOUTH, new Vector3f(.5f, .5f, 2f / 16f),
				Direction.WEST, new Vector3f(14f / 16f, .5f, .5f)
		);
	}
}
