package com.mrcabbagestick.electrify.content.wire_connectors;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.content.network.Network;
import com.mrcabbagestick.electrify.tools.NbtTools;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

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
	public UUID networkNodeUuid;
	public Network network;
	public WireConnectorBlockEntity networkProvider = this;
	private boolean isNetworkController;

	public WireConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);

		networkNodeUuid = UUID.randomUUID();
//		createOwnNetwork();
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
	}

	@Override
	protected void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);

		ListTag connectedFromList = nbt.getList("connectedFrom", CompoundTag.TAG_COMPOUND);
		ListTag connectedToList = nbt.getList("connectedTo", CompoundTag.TAG_COMPOUND);
		ListTag renderToList = nbt.getList("renderTo", CompoundTag.TAG_COMPOUND);

		connectedFrom = NbtTools.toBlockPosSet(connectedFromList);
		connectedTo = NbtTools.toBlockPosSet(connectedToList);
		renderTo = NbtTools.toFloatVectorSet(renderToList);

//		String networkNodeUuidString = nbt.getString("networkNodeUUID");
//		if(!networkNodeUuidString.isEmpty())
//			networkNodeUuid = UUID.fromString(networkNodeUuidString);

//		isNetworkController = nbt.getBoolean("isNetworkController");
//		CompoundTag networkTag = nbt.getCompound("Network");
//		BlockPos networkProviderPos = NbtTools.toBlockPos(nbt.getCompound("networkProvider"));

//		Electrify.LOGGER.info("Connector Here on client and server");

//		if(level != null) {
////			Electrify.LOGGER.info("Hello from non null level");
////			if (isNetworkController && !networkTag.isEmpty()) {
////				network = Network.fromCompoundTag(networkTag, level);
////			}
//
//			if (!isNetworkController) {
//				// networkNode: 03ca1663-0f86-477d-bca7-f7878d374d72
//
//				if(level.getBlockEntity(networkProviderPos) instanceof WireConnectorBlockEntity networkProviderEntity){
//					networkProvider = networkProviderEntity;
//					network = networkProvider.network;
//				}
//				else{
//					createOwnNetwork();
//				}
//			}
//
//			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
//		}

	}

//	protected void createOwnNetwork(){
//		network = new Network();
//		network.addNode(this);
//		isNetworkController = true;
//	}

//	public void tryToCreateOwnNetwork(){
//		if(network == null)
//			createOwnNetwork();
//	}

	@Override
	protected void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);

		nbt.put("connectedTo", NbtTools.from(connectedTo));
		nbt.put("connectedFrom", NbtTools.from(connectedFrom));
		nbt.put("renderTo", NbtTools.fromVectorSet(renderTo));

		nbt.putString("networkNodeUUID", networkNodeUuid.toString());

		nbt.putBoolean("isNetworkController", isNetworkController);
		nbt.put("networkProvider", NbtTools.from(networkProvider.getBlockPos()));

//		if (isNetworkController && network != null) {
//			nbt.put("Network", network.asCompoundTag());
//		}
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

//			this.network.mergeWith(target.network);
//			target.isNetworkController = false;
//			target.network = this.network;
//			target.networkProvider = this;

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
