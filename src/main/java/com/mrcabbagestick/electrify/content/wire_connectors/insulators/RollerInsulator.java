package com.mrcabbagestick.electrify.content.wire_connectors.insulators;

import com.mrcabbagestick.electrify.content.wire_connectors.FacingWireConnectorBlockBase;
import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import com.mrcabbagestick.electrify.entries.ElectrifyBlockEntities;

import com.mrcabbagestick.electrify.tools.RotationDegrees;
import com.mrcabbagestick.electrify.tools.VoxelShapeTools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.joml.Vector3f;

import java.util.Map;

public class RollerInsulator extends FacingWireConnectorBlockBase<WireConnectorBlockEntity> {

	public static final Map<Direction, VoxelShape> SHAPE_MAP;
	public static final Map<Direction, Vector3f> CONNECTOR_OFFSET_MAP;
	private static final VoxelShape BASE_VERTICAL_SHAPE;
	private static final VoxelShape BASE_HORIZONTAL_SHAPE;
	public RollerInsulator(Properties properties) {
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

	static {
		BASE_VERTICAL_SHAPE = Shapes.box(6.5f / 16.0f, 0.0f, 6.5f / 16.0f, 9.5f / 16.0f, 7.5f / 16.0f, 9.5f / 16.0f);
		BASE_HORIZONTAL_SHAPE = Shapes.box(6.5f / 16.0f, 5.5f / 16.0f, 0.0f, 9.5f / 16.0f, 10.5f / 16.0f, 5.5f / 16.0f);
		SHAPE_MAP = Map.of(
				Direction.NORTH, BASE_HORIZONTAL_SHAPE,
				Direction.EAST, VoxelShapeTools.rotate(BASE_HORIZONTAL_SHAPE, RotationDegrees.DEG90, Direction.Axis.Y),
				Direction.SOUTH, VoxelShapeTools.rotate(BASE_HORIZONTAL_SHAPE, RotationDegrees.DEG180, Direction.Axis.Y),
				Direction.WEST, VoxelShapeTools.rotate(BASE_HORIZONTAL_SHAPE, RotationDegrees.DEG270, Direction.Axis.Y),

				Direction.DOWN, BASE_VERTICAL_SHAPE,
				Direction.UP, VoxelShapeTools.rotate(BASE_VERTICAL_SHAPE, RotationDegrees.DEG180, Direction.Axis.X)
		);
		CONNECTOR_OFFSET_MAP = Map.of(
				Direction.NORTH, new Vector3f(8.0f / 16.0f, 8.0f / 16.0f,4.0f / 16.0f),
				Direction.EAST, new Vector3f(12.0f / 16.0f, 8.0f / 16.0f,8.0f / 16.0f),
				Direction.SOUTH, new Vector3f(8.0f / 16.0f, 8.0f / 16.0f,12.0f / 16.0f),
				Direction.WEST, new Vector3f(4.0f / 16.0f, 8.0f / 16.0f, 8.0f / 16.0f),

				Direction.DOWN, new Vector3f(8.0f / 16.0f, 5.0f / 16.0f, 8.0f / 16.0f),
				Direction.UP, new Vector3f(8.0f / 16.0f, 11.0f / 16.0f, 8.0f / 16.0f)
		);
	}
}
