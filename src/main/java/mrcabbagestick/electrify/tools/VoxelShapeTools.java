package mrcabbagestick.electrify.tools;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;

public class VoxelShapeTools {

    public static VoxelShape rotate(VoxelShape shape, RotationDegrees degrees, Direction.Axis rotationAxis){
        ArrayList<VoxelShape> outputShapes = new ArrayList<>();
        shape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
            outputShapes.add(
                    switch (rotationAxis){
                        case X -> switch (degrees){
                            case DEG0, DEG360 -> VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ);
                            case DEG90 -> VoxelShapes.cuboid(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY);
                            case DEG180 -> VoxelShapes.cuboid(minX, 1 - maxY, 1 - maxZ, maxX, 1 - minY, 1 - minZ);
                            case DEG270 -> VoxelShapes.cuboid(minX,  minZ, 1 - maxY, maxX, maxZ, 1 - minY);
                        };
                        case Z -> switch (degrees){
                            case DEG0, DEG360 -> VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ);
                            case DEG90 -> VoxelShapes.cuboid(minY, 1 - maxX, minZ, maxY, 1 - minX, maxZ);
                            case DEG180 -> VoxelShapes.cuboid( 1 - maxX, 1 - maxY, minZ, 1 - minX, 1 - minY, maxZ);
                            case DEG270 -> VoxelShapes.cuboid(1 - maxY,  minX, minZ, 1 - minY, maxZ, maxX);
                        };
                        case Y -> switch (degrees){
                            case DEG0, DEG360 -> VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ);
                            case DEG90 -> VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX);
                            case DEG180 -> VoxelShapes.cuboid(1 - maxX, minY, 1 - maxZ, 1 - minX, maxY, 1 - minZ);
                            case DEG270 -> VoxelShapes.cuboid(minZ, minY, 1 - maxX, maxZ, maxY, 1 - minX);
                        };
                    }
            );
        });

        return (outputShapes.size() > 1)
                ? VoxelShapes.union(outputShapes.get(0), (VoxelShape)outputShapes.subList(1, outputShapes.size()))
                : outputShapes.get(0);
    }

}

