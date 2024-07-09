package com.mrcabbagestick.electrify.content.wire_connectors;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.content.network.Network;
import com.mrcabbagestick.electrify.content.network.NetworkController;
import com.mrcabbagestick.electrify.content.network.NetworkNode;
import com.mrcabbagestick.electrify.tools.NbtTools;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.joml.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WireConnectorBlockEntity extends SmartBlockEntity {

	public Set<BlockPos> connectedFrom = new HashSet<>();
	public Set<BlockPos> connectedTo = new HashSet<>();
	public Set<Vector3f> renderTo = new HashSet<>();

	public NetworkNode networkNode;

	public WireConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);

		networkNode = NetworkNode.createWithNetwork();
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

	@Override
	protected void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);

		ListTag connectedFromList = nbt.getList("connectedFrom", CompoundTag.TAG_COMPOUND);
		ListTag connectedToList = nbt.getList("connectedTo", CompoundTag.TAG_COMPOUND);
		ListTag renderToList = nbt.getList("renderTo", CompoundTag.TAG_COMPOUND);

		connectedFrom = NbtTools.toBlockPosSet(connectedFromList);
		connectedTo = NbtTools.toBlockPosSet(connectedToList);
		renderTo = NbtTools.toFloatVectorSet(renderToList);

		UUID networkNodeUuid;
		UUID networkUuid;

		if(
			(networkNodeUuid = NbtTools.getUUID("networkNodeUUID", nbt)) == null
			|| (networkUuid = NbtTools.getUUID("networkUUID", nbt)) == null
		){
			Electrify.LOGGER.warn("WireConnectorBlockEntity loaded without network part");
			return;
		}

		var network = NetworkController.getNetwork(networkUuid);
		if(network == null) {
			Electrify.LOGGER.error("WireConnectorBlockEntity could not find it's network");
			return;
		}

		networkNode = network.getNode(networkNodeUuid);
		if(networkNode == null)
			Electrify.LOGGER.error("WireConnectorBlockEntity could not find it's node");
	}

	@Override
	protected void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);

		nbt.put("connectedTo", NbtTools.from(connectedTo));
		nbt.put("connectedFrom", NbtTools.from(connectedFrom));
		nbt.put("renderTo", NbtTools.fromVectorSet(renderTo));

		nbt.putUUID("networkNodeUUID", networkNode.uuid);
		nbt.putUUID("networkUUID", networkNode.getNetwork().getUuid());
	}

	@Override
	public void remove() {

		for(BlockPos from : connectedFrom){
			if(level.getBlockEntity(from) instanceof WireConnectorBlockEntity fromConnector) {
				fromConnector.connectedTo.remove(getBlockPos());
				fromConnector.updateRenderPositions();
				fromConnector.setChanged();
			}
		}

		for(BlockPos to : connectedTo){
			if(level.getBlockEntity(to) instanceof WireConnectorBlockEntity toConnector) {
				toConnector.connectedFrom.remove(getBlockPos());
				toConnector.updateRenderPositions();
				toConnector.setChanged();
			}
		}

		super.remove();
	}

	public boolean connectionFrom(BlockPos sourcePos) {

		if (sourcePos.distSqr(getBlockPos()) != 0 && connectedFrom.add(sourcePos)){
			this.setChanged();
			return true;
		}

		return false;
	}

	public boolean connectTo(WireConnectorBlockEntity target){
		BlockPos targetPos = target.getBlockPos();

		if(targetPos.distSqr(getBlockPos()) != 0 && !connectedFrom.contains(targetPos) && connectedTo.add(targetPos)){

			updateRenderPositions();
			setChanged();

			return true;
		}

		return false;
	}

	public void updateRenderPositions(){
		renderTo.clear();

		for(BlockPos toCheck : connectedTo){

			BlockState stateToCheck = level.getBlockState(toCheck);

			if(stateToCheck.getBlock() instanceof WireConnectorBaseBlock<?> connector){
				BlockPos relativeBlockPos = toCheck.subtract(getBlockPos());
				renderTo.add(new Vector3f(relativeBlockPos.getX(), relativeBlockPos.getY(), relativeBlockPos.getZ())
						.add(connector.getConnectorOffset(stateToCheck))
				);
			}

		}
	}

}
