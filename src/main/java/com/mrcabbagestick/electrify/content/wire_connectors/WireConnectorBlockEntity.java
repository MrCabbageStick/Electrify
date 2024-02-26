package com.mrcabbagestick.electrify.content.wire_connectors;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.content.network.Network;
import com.mrcabbagestick.electrify.content.wires.WireType;
import com.mrcabbagestick.electrify.tools.NbtTools;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.joml.Vector3f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WireConnectorBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

	// Rendering
	public Set<BlockPos> connectedFrom = new HashSet<>();
	public Set<BlockPos> connectedTo = new HashSet<>();
	public Set<Vector3f> renderTo = new HashSet<>();

	// Network
	public UUID networkNodeUuid;
	public Network network;

	public WireConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);

		networkNodeUuid = UUID.randomUUID();
		network = new Network(this);
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

		// Network Tag
		CompoundTag networkTag = nbt.getCompound("NetworkTag");
		networkNodeUuid = networkTag.getUUID("networkNodeUuid");
		boolean isNetworkController = networkTag.getBoolean("isNetworkController");

		if(isNetworkController && (level == null || !level.isClientSide())){
			network = Network.readFromCompoundTag(networkTag, level);
		}

	}

	@Override
	protected void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);

		nbt.put("connectedTo", NbtTools.from(connectedTo));
		nbt.put("connectedFrom", NbtTools.from(connectedFrom));
		nbt.put("renderTo", NbtTools.fromVectorSet(renderTo));

		// Network Tag
		if(level != null && !level.isClientSide()){

			network.initializeNodes(level);

			CompoundTag networkTag = new CompoundTag();
			networkTag.putUUID("networkNodeUuid", networkNodeUuid);
			networkTag.putBoolean("isNetworkController", network.isNetworkController(networkNodeUuid));

			if(network.isNetworkController(networkNodeUuid))
				network.writeToCompoundTag(networkTag);

			nbt.put("NetworkTag", networkTag);
		}
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


			// Handle network stuff
			Network.LinkAddResponse linkAdd = network.addLink(this, target, WireType.STEEL_WIRE);

			switch(linkAdd){
				case NETWORKS_MERGED -> target.network = network;
				case NETWORKS_MERGED_REVERSED -> network = target.network;
			}

			// Handle render stuff
			updateRenderPositions();

			setChanged();
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_NEIGHBORS);

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

//	@Override
//	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//
//		Lang.text("Electrical Network").forGoggles(tooltip);
//		Lang.text(String.valueOf(network.allNodes.size()))
//				.style(ChatFormatting.AQUA)
//				.add(Lang.text(" network nodes").style(ChatFormatting.DARK_GRAY))
//				.forGoggles(tooltip);
//
//		Lang.text(String.valueOf(network.allLinks.size() / 2))
//				.style(ChatFormatting.AQUA)
//				.add(Lang.text(" network links").style(ChatFormatting.DARK_GRAY))
//				.forGoggles(tooltip);
//
//		BlockPos controllerPos = network.allNodes.get(network.networkController).connectorEntity.getBlockPos();
//		Lang.text("x: " + controllerPos.getX() + " y: " + controllerPos.getY() + " z: " + controllerPos.getZ())
//				.style(ChatFormatting.AQUA)
//				.add(Lang.text(" controller position").style(ChatFormatting.DARK_GRAY))
//				.forGoggles(tooltip);
//
//		return true;
//	}
}
