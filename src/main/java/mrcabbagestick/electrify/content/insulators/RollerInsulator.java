package mrcabbagestick.electrify.content.insulators;

import com.google.common.collect.ImmutableMap;
import mrcabbagestick.electrify.tools.RotationDegrees;
import mrcabbagestick.electrify.tools.VoxelShapeTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
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
import org.joml.Vector3f;

import java.util.Map;

public class RollerInsulator extends Block implements BlockEntityProvider, IWireConnector {

    public static final Map<Direction, VoxelShape> SHAPE_MAP;
    public static final Map<Direction, Vector3f> CONNECTION_OFFSET_MAP;
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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RollerInsulatorBlockEntity(pos, state);
    }

    @Override
    public Vector3f getConnectorOffset(BlockState state) {
        return CONNECTION_OFFSET_MAP.get(state.get(FACING));
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

        CONNECTION_OFFSET_MAP = Map.of(
                Direction.NORTH, new Vector3f(8.0f / 16.0f, 8.0f / 16.0f,4.0f / 16.0f),
                Direction.EAST, new Vector3f(12.0f / 16.0f, 8.0f / 16.0f,8.0f / 16.0f),
                Direction.SOUTH, new Vector3f(8.0f / 16.0f, 8.0f / 16.0f,12.0f / 16.0f),
                Direction.WEST, new Vector3f(4.0f / 16.0f, 8.0f / 16.0f, 8.0f / 16.0f),

                Direction.DOWN, new Vector3f(8.0f / 16.0f, 5.0f / 16.0f, 8.0f / 16.0f),
                Direction.UP, new Vector3f(8.0f / 16.0f, 11.0f / 16.0f, 8.0f / 16.0f)
        );
    }
}
