package com.mrcabbagestick.electrify.tools;

import java.util.ArrayList;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeTools {
	public static VoxelShape rotate(VoxelShape shape, RotationDegrees degrees, Direction.Axis rotationAxis){
		ArrayList<VoxelShape> outputShapes = new ArrayList<>();
		shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
			outputShapes.add(
					switch (rotationAxis){
						case X -> switch (degrees){
							case DEG0, DEG360 -> Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
							case DEG90 -> Shapes.box(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY);
							case DEG180 -> Shapes.box(minX, 1 - maxY, 1 - maxZ, maxX, 1 - minY, 1 - minZ);
							case DEG270 -> Shapes.box(minX,  minZ, 1 - maxY, maxX, maxZ, 1 - minY);
						};
						case Z -> switch (degrees){
							case DEG0, DEG360 -> Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
							case DEG90 -> Shapes.box(minY, 1 - maxX, minZ, maxY, 1 - minX, maxZ);
							case DEG180 -> Shapes.box( 1 - maxX, 1 - maxY, minZ, 1 - minX, 1 - minY, maxZ);
							case DEG270 -> Shapes.box(1 - maxY,  minX, minZ, 1 - minY, maxZ, maxX);
						};
						case Y -> switch (degrees){
							case DEG0, DEG360 -> Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
							case DEG90 -> Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX);
							case DEG180 -> Shapes.box(1 - maxX, minY, 1 - maxZ, 1 - minX, maxY, 1 - minZ);
							case DEG270 -> Shapes.box(minZ, minY, 1 - maxX, maxZ, maxY, 1 - minX);
						};
					}
			);
		});

		return (outputShapes.size() > 1)
				? Shapes.or(outputShapes.get(0), (VoxelShape)outputShapes.subList(1, outputShapes.size()))
				: outputShapes.get(0);
	}
}
