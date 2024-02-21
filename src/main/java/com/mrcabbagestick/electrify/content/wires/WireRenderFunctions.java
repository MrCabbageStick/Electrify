package com.mrcabbagestick.electrify.content.wires;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.core.Direction;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WireRenderFunctions {

	public static float centenary(float segmentIndex, float entireLength, int segmentCount, float wireLength, float sagFactor) {
		float a = entireLength / wireLength * sagFactor;
		segmentIndex = (segmentIndex / segmentCount * 2 - 1); // (segmentIndex / sections) = <0, 1> -> *2 -> <0, 2> -> -1 -> < -1, 1>
		return (float) ((Math.cosh(segmentIndex) - Math.cosh(1.0f)) * a);
	}

	public static void renderWire(Vector3f startPos, Vector3f targetPos, WireType wireType, Matrix4f positionMatrix, VertexConsumer buffer, int light, int overlay){

		Vector3f lastSegmentEnd = startPos;
		Vector3f direction = new Vector3f(targetPos).sub(startPos).normalize();
		boolean isVertical = direction.equals(.0f, 1.0f, .0f) || direction.equals(.0f, -1.0f, .0f);

		float distance = startPos.distance(targetPos);
		int segmentCount = (int) Math.ceil(distance * WireType.SEGMENTS_PER_BLOCK);
		float segmentLength = distance / segmentCount;

		if(isVertical)
			for(int i = 0; i <= segmentCount; i++){
				Vector3f segmentEnd = new Vector3f(direction).mul(segmentLength * i);
				renderWirePartY(lastSegmentEnd, segmentEnd.add(startPos), positionMatrix, WireType.HALF_WIRE_WIDTH, wireType.getColors().get(i % 2), buffer, light, overlay);
				lastSegmentEnd = segmentEnd;
			}

		else
			for(int i = 0; i <= segmentCount; i++){
				Vector3f segmentEnd = new Vector3f(direction).mul(segmentLength * i).add(0, centenary(i, distance, segmentCount, WireType.WIRE_LENGTH, WireType.SAG_FACTOR), 0);
				renderWirePart(lastSegmentEnd, segmentEnd.add(startPos), positionMatrix, WireType.HALF_WIRE_WIDTH, wireType.getColors().get(i % 2), buffer, light, overlay);
				lastSegmentEnd = segmentEnd;
			}

	}

	public static void renderWirePart(Vector3f from, Vector3f to, Matrix4f positionMatrix, float widthOffset, int color, VertexConsumer buffer, int light, int overlay){

		Vector3f direction = new Vector3f(to).sub(from).normalize();
		float length = from.distance(to);

		Matrix4f localPositionMatrix = new Matrix4f(positionMatrix);
		localPositionMatrix.translate(from);

		localPositionMatrix.rotateTowards(direction, Direction.UP.step()); // Z looks towards end pos
		buffer.vertex(localPositionMatrix, widthOffset, widthOffset, 0).color(color).uv2(light).endVertex();
		buffer.vertex(localPositionMatrix, -widthOffset, -widthOffset, 0).color(color).uv2(light).endVertex();
		buffer.vertex(localPositionMatrix, -widthOffset, -widthOffset, length).color(color).uv2(light).endVertex();
		buffer.vertex(localPositionMatrix, widthOffset, widthOffset, length).color(color).uv2(light).endVertex();

		buffer.vertex(localPositionMatrix, -widthOffset, widthOffset, 0).color(color).uv2(light).endVertex();
		buffer.vertex(localPositionMatrix, widthOffset, -widthOffset, 0).color(color).uv2(light).endVertex();
		buffer.vertex(localPositionMatrix, widthOffset, -widthOffset, length).color(color).uv2(light).endVertex();
		buffer.vertex(localPositionMatrix, -widthOffset, widthOffset, length).color(color).uv2(light).endVertex();
	}

	public static void renderWirePartY(Vector3f from, Vector3f to, Matrix4f positionMatrix, float widthOffset, int color, VertexConsumer buffer, int light, int overlay){
		buffer.vertex(positionMatrix, from.x + widthOffset, from.y, from.z + widthOffset).color(color).uv2(light).endVertex();
		buffer.vertex(positionMatrix, from.x - widthOffset, from.y, from.z - widthOffset).color(color).uv2(light).endVertex();
		buffer.vertex(positionMatrix, to.x - widthOffset, to.y, to.z - widthOffset).color(color).uv2(light).endVertex();
		buffer.vertex(positionMatrix, to.x + widthOffset, to.y, to.z + widthOffset).color(color).uv2(light).endVertex();

		buffer.vertex(positionMatrix, from.x - widthOffset, from.y, from.z + widthOffset).color(color).uv2(light).endVertex();
		buffer.vertex(positionMatrix, from.x + widthOffset, from.y, from.z - widthOffset).color(color).uv2(light).endVertex();
		buffer.vertex(positionMatrix, to.x + widthOffset, to.y, to.z - widthOffset).color(color).uv2(light).endVertex();
		buffer.vertex(positionMatrix, to.x - widthOffset, to.y, to.z + widthOffset).color(color).uv2(light).endVertex();
	}

}
