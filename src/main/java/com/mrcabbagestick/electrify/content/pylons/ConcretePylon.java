package com.mrcabbagestick.electrify.content.pylons;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.mrcabbagestick.electrify.tools.RotationDegrees;
import com.mrcabbagestick.electrify.tools.VoxelShapeTools;
import com.simibubi.create.foundation.utility.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConcretePylon extends Block {

	// Axis: Y, Rotated: false -> default model
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
	public static final BooleanProperty ROTATED = BooleanProperty.create("rotated");

	public static final VoxelShape SHAPE = Shapes.box(4f / 16f, 0f, 5f / 16f, 12f / 16f, 1f, 11f / 16f);
	public static final Map<Pair<Direction.Axis, Boolean>, VoxelShape> SHAPE_MAP;

	public ConcretePylon(Properties properties) {
		super(properties);
		registerDefaultState(
				defaultBlockState()
						.setValue(AXIS, Direction.Axis.Y)
						.setValue(ROTATED, false)
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS).add(ROTATED);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		var clickedFace = context.getClickedFace();
		var playerFacing = context.getHorizontalDirection();

		Direction.Axis axis = clickedFace.getAxis();
		boolean rotated = (playerFacing.getAxis() == Direction.Axis.X) && axis.isVertical();

		return defaultBlockState().setValue(AXIS, axis).setValue(ROTATED, rotated);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if(player.getItemInHand(hand).is(ItemTags.PICKAXES)){
			level.setBlock(pos, state.setValue(ROTATED, !state.getValue(ROTATED)), 1);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_MAP.get(Pair.of(state.getValue(AXIS), state.getValue(ROTATED)));
	}

	static{
		SHAPE_MAP = Map.of(
				Pair.of(Direction.Axis.Y, false), SHAPE,
				Pair.of(Direction.Axis.Y, true), VoxelShapeTools.rotate(SHAPE, RotationDegrees.DEG90, Direction.Axis.Y),
				Pair.of(Direction.Axis.Z, false), VoxelShapeTools.rotate(
						VoxelShapeTools.rotate(SHAPE, RotationDegrees.DEG90, Direction.Axis.X),
						RotationDegrees.DEG90,
						Direction.Axis.Z
				),
				Pair.of(Direction.Axis.Z, true), VoxelShapeTools.rotate(SHAPE, RotationDegrees.DEG90, Direction.Axis.X),
				Pair.of(Direction.Axis.X, false), VoxelShapeTools.rotate(
						VoxelShapeTools.rotate(SHAPE, RotationDegrees.DEG90, Direction.Axis.X),
						RotationDegrees.DEG90,
						Direction.Axis.Y
				),
				Pair.of(Direction.Axis.X, true), VoxelShapeTools.rotate(
						VoxelShapeTools.rotate(
								VoxelShapeTools.rotate(SHAPE, RotationDegrees.DEG90, Direction.Axis.X),
								RotationDegrees.DEG90,
								Direction.Axis.Y
						),
						RotationDegrees.DEG90,
						Direction.Axis.Z
				)
		);
	}
}
