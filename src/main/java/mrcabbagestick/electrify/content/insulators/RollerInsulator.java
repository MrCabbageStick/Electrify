package mrcabbagestick.electrify.content.insulators;

import com.google.common.collect.ImmutableMap;
import mrcabbagestick.electrify.tools.RotationDegrees;
import mrcabbagestick.electrify.tools.VoxelShapeTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RollerInsulator extends Block {

    public static final Map<Direction, VoxelShape> SHAPE_MAP;
    private static final VoxelShape BASE_VERTICAL_SHAPE;
    private static final VoxelShape BASE_HORIZONTAL_SHAPE;
    public static final DirectionProperty FACING = Properties.FACING;

    public RollerInsulator(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.DOWN));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getSide().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_MAP.get(state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    static{
        BASE_VERTICAL_SHAPE = VoxelShapes.cuboid(6.5f / 16.0f, 0.0f, 6.5f / 16.0f, 9.5f / 16.0f, 7.5f / 16.0f, 9.5f / 16.0f);
        BASE_HORIZONTAL_SHAPE = VoxelShapes.cuboid(6.5f / 16.0f, 5.5f / 16.0f, 0.0f, 9.5f / 16.0f, 10.5f / 16.0f, 5.5f / 16.0f);
        SHAPE_MAP = Map.of(
                Direction.NORTH, BASE_HORIZONTAL_SHAPE,
                Direction.EAST, VoxelShapeTools.rotate(BASE_HORIZONTAL_SHAPE, RotationDegrees.DEG90, Direction.Axis.Y),
                Direction.SOUTH, VoxelShapeTools.rotate(BASE_HORIZONTAL_SHAPE, RotationDegrees.DEG180, Direction.Axis.Y),
                Direction.WEST, VoxelShapeTools.rotate(BASE_HORIZONTAL_SHAPE, RotationDegrees.DEG270, Direction.Axis.Y),

                Direction.DOWN, BASE_VERTICAL_SHAPE,
                Direction.UP, VoxelShapeTools.rotate(BASE_VERTICAL_SHAPE, RotationDegrees.DEG180, Direction.Axis.X)
        );
    }
}
