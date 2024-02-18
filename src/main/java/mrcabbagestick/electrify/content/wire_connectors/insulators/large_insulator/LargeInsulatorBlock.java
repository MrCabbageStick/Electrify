package mrcabbagestick.electrify.content.wire_connectors.insulators.large_insulator;

import mrcabbagestick.electrify.content.wire_connectors.FacingWireConnectorBlock;
import mrcabbagestick.electrify.tools.RotationDegrees;
import mrcabbagestick.electrify.tools.VoxelShapeTools;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;

public class LargeInsulatorBlock extends FacingWireConnectorBlock {

    public static final Map<Direction, VoxelShape> SHAPE_MAP;
    public static final Map<Direction, Vector3f> CONNECTION_OFFSET_MAP;
    private static final VoxelShape BASE_SHAPE;
    private static final VoxelShape HORIZONTAL_SHAPE;
    public LargeInsulatorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Vector3f getConnectorOffset(BlockState state) {
        return CONNECTION_OFFSET_MAP.get(state.get(FACING));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LargeInsulatorBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_MAP.get(state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE_MAP.get(state.get(FACING));
    }

    static{
        BASE_SHAPE = VoxelShapes.cuboid(5f / 16f, 0f, 5f / 16f, 11f / 16f, 14.5f / 16f, 11f / 16f);
        HORIZONTAL_SHAPE = VoxelShapeTools.rotate(BASE_SHAPE, RotationDegrees.DEG90, Direction.Axis.X);
        SHAPE_MAP = Map.of(
                Direction.DOWN, BASE_SHAPE,
                Direction.UP, VoxelShapeTools.rotate(BASE_SHAPE, RotationDegrees.DEG180, Direction.Axis.X),

                Direction.NORTH, HORIZONTAL_SHAPE,
                Direction.EAST, VoxelShapeTools.rotate(HORIZONTAL_SHAPE, RotationDegrees.DEG90, Direction.Axis.Y),
                Direction.SOUTH, VoxelShapeTools.rotate(HORIZONTAL_SHAPE, RotationDegrees.DEG180, Direction.Axis.Y),
                Direction.WEST, VoxelShapeTools.rotate(HORIZONTAL_SHAPE, RotationDegrees.DEG270, Direction.Axis.Y)
        );
        CONNECTION_OFFSET_MAP = Map.of(
                Direction.DOWN, new Vector3f(.5f, 14f / 16f, .5f),
                Direction.UP, new Vector3f(.5f, 2f / 16f, .5f),

                Direction.NORTH, new Vector3f(.5f, .5f, 14f / 16f),
                Direction.EAST, new Vector3f(2f / 16f, .5f, .5f),
                Direction.SOUTH, new Vector3f(.5f, .5f, 2f / 16f),
                Direction.WEST, new Vector3f(14f / 16f, .5f, .5f)
        );
    }
}
