package com.mrcabbagestick.electrify.content.generators.small_dynamo;

import com.mrcabbagestick.electrify.entries.ElectrifyBlockEntities;
import com.mrcabbagestick.electrify.entries.ElectrifyBlocks;
import com.mrcabbagestick.electrify.tools.RotationDegrees;
import com.mrcabbagestick.electrify.tools.VoxelShapeTools;

import com.simibubi.create.AllBlockEntityTypes;

import com.simibubi.create.AllBlocks;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;

import com.simibubi.create.foundation.block.IBE;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SmallDynamoBlock extends DirectionalKineticBlock implements IBE<SmallDynamoBlockEntity>, IHaveGoggleInformation {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final VoxelShape BASE_SHAPE;
	public static final VoxelShape SHAPE_VERTICAL;
	public static final Map<Direction, VoxelShape> SHAPE_MAP;
	public SmallDynamoBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction preferred = getPreferredFacing(context);

		if (preferred == null || (context.getPlayer() != null && context.getPlayer().isShiftKeyDown())) {

			Direction nearestLookingDirection = context.getNearestLookingDirection();

			return defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer()
					.isShiftKeyDown() ? nearestLookingDirection.getOpposite() : nearestLookingDirection);
		}

		return defaultBlockState().setValue(FACING, preferred);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_MAP.get(state.getValue(FACING));
	}

	@Override
	public Direction.Axis getRotationAxis(BlockState state) {
		return state.getValue(FACING).getAxis();
	}

	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == state.getValue(FACING);
	}

	@Override
	public Class<SmallDynamoBlockEntity> getBlockEntityClass() {
		return SmallDynamoBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SmallDynamoBlockEntity> getBlockEntityType() {
		return ElectrifyBlockEntities.SMALL_DYNAMO_BLOCK_ENTITY.get();
	}

	static {
		BASE_SHAPE = Shapes.box(1f / 16f, 0f, 1f / 16f, 15f / 16f, 14f / 16f, 1f);
		SHAPE_VERTICAL = Shapes.box(2f / 16f, 0f, 2f / 16f, 14f / 16f, 15f / 16f, 14f / 16f);

		SHAPE_MAP = Map.of(
				Direction.NORTH, BASE_SHAPE,
				Direction.EAST, VoxelShapeTools.rotate(BASE_SHAPE, RotationDegrees.DEG90, Direction.Axis.Y),
				Direction.SOUTH, VoxelShapeTools.rotate(BASE_SHAPE, RotationDegrees.DEG180, Direction.Axis.Y),
				Direction.WEST, VoxelShapeTools.rotate(BASE_SHAPE, RotationDegrees.DEG270, Direction.Axis.Y),

				Direction.UP, SHAPE_VERTICAL,
				Direction.DOWN, VoxelShapeTools.rotate(SHAPE_VERTICAL, RotationDegrees.DEG180, Direction.Axis.X)
		);
	}
}
