package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import com.mrcabbagestick.electrify.content.wires.WireType;

import com.mrcabbagestick.electrify.tools.NbtTools;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

public class NetworkNode {
	public WireConnectorBlockEntity connectorEntity;
	public NodeType type;

	NetworkNode(WireConnectorBlockEntity entity){
		connectorEntity = entity;
		type = NodeType.IDLE;
	}

	public void setType(NodeType newType){
		type = newType;
	}

	public NodeType getType(){
		return type;
	}

	public static enum NodeType{

		IDLE("idle"),
		GENERATOR("generator"),
		CONSUMER("consumer");
		private final String name;
		private static final Map<String, NodeType> nameMap = new HashMap<>();
		NodeType(String name){ this.name = name; }

		public String asString(){ return this.name; }
		public static NodeType fromString(String name){ return nameMap.get(name); }

		static {
			for(NodeType type : NodeType.values())
				nameMap.put(type.name, type);
		}
	}

	// ADDITIONAL
	public void writeToCompoundTag(CompoundTag nbt){
		nbt.put("connectorPos", NbtTools.from(connectorEntity.getBlockPos()));
		nbt.putString("type", type.asString());
	}

	@Nullable
	public static NetworkNode readFromCompoundTag(CompoundTag tag, Level world){

		BlockEntity entity = world.getBlockEntity(NbtTools.toBlockPos(tag.getCompound("connectorPos")));

		if(entity instanceof WireConnectorBlockEntity connectorEntity){
			NetworkNode node = new NetworkNode(connectorEntity);

			node.setType(NodeType.fromString(tag.getString("type")));

			return node;
		}

		return null;
	}


	public static class UninitializedNetworkNode{
		public BlockPos connectorEntityBlockPos;
		public NodeType type;

		public UninitializedNetworkNode(BlockPos entityPos){
			connectorEntityBlockPos = entityPos;
		}
		public void setType(NodeType type) {
			this.type = type;
		}

		@Nullable
		public NetworkNode initialize(@Nonnull Level world){
			BlockEntity entity = world.getBlockEntity(connectorEntityBlockPos);

			if(entity instanceof WireConnectorBlockEntity connectorEntity){
				NetworkNode node = new NetworkNode(connectorEntity);
				node.setType(type);

				return node;
			}

			return null;
		}

		public void writeToCompoundTag(CompoundTag nbt){
			nbt.put("connectorPos", NbtTools.from(connectorEntityBlockPos));
			nbt.putString("type", type.asString());
		}

		public static UninitializedNetworkNode readFromCompoundTag(CompoundTag tag){

			UninitializedNetworkNode node = new UninitializedNetworkNode(NbtTools.toBlockPos(tag.getCompound("connectorPos")));
			node.setType(NodeType.fromString(tag.getString("type")));
			return node;
		}
	}
}
