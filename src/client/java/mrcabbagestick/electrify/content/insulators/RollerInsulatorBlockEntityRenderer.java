package mrcabbagestick.electrify.content.insulators;

import mrcabbagestick.electrify.Electrify;
import mrcabbagestick.electrify.ElectrifyBlockTags;
import mrcabbagestick.electrify.ElectrifyBlocks;
import mrcabbagestick.electrify.content.ElectrifyRenderTypes;
import mrcabbagestick.electrify.content.wires.WireTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.http.impl.conn.Wire;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class RollerInsulatorBlockEntityRenderer implements BlockEntityRenderer<RollerInsulatorBlockEntity> {

    private ArrayList<Vector3f> connectedToOffset = new ArrayList<>();

    public RollerInsulatorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx){}


    private ArrayList<Vector3f> blockPosListToOffsetList(ArrayList<BlockPos> blockPoses, BlockPos entityPos){
        return new ArrayList<>(blockPoses.stream().map(pos -> {

            BlockState state = MinecraftClient.getInstance().world.getBlockState(pos);

            if(state != null && state.isIn(ElectrifyBlockTags.WIRE_CONNECTORS)){
                Vector3f offset = ((IWireConnector)state.getBlock()).getConnectorOffset(state);

                return new Vector3f(pos.getX(), pos.getY(), pos.getZ())
                        .add(offset)
                        .sub(entityPos.getX(), entityPos.getY(), entityPos.getZ());
            }

            return null;

        }).toList());
    }
    public void updateOffsets(RollerInsulatorBlockEntity entity){
        connectedToOffset = blockPosListToOffsetList(entity.connectedTo, entity.getPos());
    }

    @Override
    public void render(RollerInsulatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        matrices.push();

        updateOffsets(entity);

        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        VertexConsumer buffer = vertexConsumers.getBuffer(ElectrifyRenderTypes.WIRE);

        for(Vector3f endPos : connectedToOffset){

            if(endPos == null) continue;

            Vector3f startPos = RollerInsulator.CONNECTION_OFFSET_MAP.get(entity.getCachedState().get(RollerInsulator.FACING));
            renderWire(startPos, endPos, WireTypes.STEEL_WIRE, positionMatrix, buffer, light, overlay);
        }

        matrices.pop();
    }

    protected void renderWire(Vector3f startPos, Vector3f targetPos, WireTypes wireType, Matrix4f positionMatrix, VertexConsumer buffer, int light, int overlay){

        Vector3f lastSegmentEnd = startPos;
        Vector3f direction = new Vector3f(targetPos).sub(startPos).normalize();
        boolean isVertical = direction.equals(.0f, 1.0f, .0f) || direction.equals(.0f, -1.0f, .0f);

        float distance = startPos.distance(targetPos);
        int segmentCount = (int) Math.ceil(distance * WireTypes.SEGMENTS_PER_BLOCK);
        float segmentLength = distance / segmentCount;

        if(isVertical)
            for(int i = 0; i <= segmentCount; i++){
                Vector3f segmentEnd = new Vector3f(direction).mul(segmentLength * i);
                renderWirePartY(lastSegmentEnd, segmentEnd.add(startPos), positionMatrix, WireTypes.HALF_WIRE_WIDTH, wireType.getColors().get(i % 2), buffer, light, overlay);
                lastSegmentEnd = segmentEnd;
            }

        else
            for(int i = 0; i <= segmentCount; i++){
                Vector3f segmentEnd = new Vector3f(direction).mul(segmentLength * i).add(0, WireTypes.centenary(i, distance, segmentCount), 0);
                renderWirePart(lastSegmentEnd, segmentEnd.add(startPos), positionMatrix, WireTypes.HALF_WIRE_WIDTH, wireType.getColors().get(i % 2), buffer, light, overlay);
                lastSegmentEnd = segmentEnd;
            }

    }

    protected void renderWirePart(Vector3f from, Vector3f to, Matrix4f positionMatrix, float widthOffset, int color, VertexConsumer buffer, int light, int overlay){

        Vector3f direction = new Vector3f(to).sub(from).normalize();
        float length = from.distance(to);

        Matrix4f localPositionMatrix = new Matrix4f(positionMatrix);
        localPositionMatrix.translate(from);

        localPositionMatrix.rotateTowards(direction, Direction.UP.getUnitVector()); // Z looks towards end pos
        buffer.vertex(localPositionMatrix, widthOffset, widthOffset, 0).color(color).light(light).next();
        buffer.vertex(localPositionMatrix, -widthOffset, -widthOffset, 0).color(color).light(light).next();
        buffer.vertex(localPositionMatrix, -widthOffset, -widthOffset, length).color(color).light(light).next();
        buffer.vertex(localPositionMatrix, widthOffset, widthOffset, length).color(color).light(light).next();

        buffer.vertex(localPositionMatrix, -widthOffset, widthOffset, 0).color(color).light(light).next();
        buffer.vertex(localPositionMatrix, widthOffset, -widthOffset, 0).color(color).light(light).next();
        buffer.vertex(localPositionMatrix, widthOffset, -widthOffset, length).color(color).light(light).next();
        buffer.vertex(localPositionMatrix, -widthOffset, widthOffset, length).color(color).light(light).next();
    }

    protected void renderWirePartY(Vector3f from, Vector3f to, Matrix4f positionMatrix, float widthOffset, int color, VertexConsumer buffer, int light, int overlay){
        buffer.vertex(positionMatrix, from.x + widthOffset, from.y, from.z + widthOffset).color(color).light(light).next();
        buffer.vertex(positionMatrix, from.x - widthOffset, from.y, from.z - widthOffset).color(color).light(light).next();
        buffer.vertex(positionMatrix, to.x - widthOffset, to.y, to.z - widthOffset).color(color).light(light).next();
        buffer.vertex(positionMatrix, to.x + widthOffset, to.y, to.z + widthOffset).color(color).light(light).next();

        buffer.vertex(positionMatrix, from.x - widthOffset, from.y, from.z + widthOffset).color(color).light(light).next();
        buffer.vertex(positionMatrix, from.x + widthOffset, from.y, from.z - widthOffset).color(color).light(light).next();
        buffer.vertex(positionMatrix, to.x + widthOffset, to.y, to.z - widthOffset).color(color).light(light).next();
        buffer.vertex(positionMatrix, to.x - widthOffset, to.y, to.z + widthOffset).color(color).light(light).next();
    }
}
