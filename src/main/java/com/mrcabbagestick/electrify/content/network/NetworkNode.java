package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import com.mrcabbagestick.electrify.tools.NbtTools;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

public class NetworkNode {

	private NodeType type;
	private WireConnectorBlockEntity connectorEntity;

	public NetworkNode(WireConnectorBlockEntity connectorEntity, NodeType type){
		this.connectorEntity = connectorEntity;
		this.type = type;
	}

	public void changeType(NodeType newType){
		this.type = newType;
	}

	public NodeType getType(){
		return type;
	}

	public WireConnectorBlockEntity getConnectorEntity(){
		return connectorEntity;
	}


	public void asCompoundTag(CompoundTag nbt){
		nbt.putString("type", type.asString());
		nbt.put("entityPos", NbtTools.from(connectorEntity.getBlockPos()));
	}

	public static NetworkNode fromCompoundTag(CompoundTag nbt, Level level){
		BlockPos entityPos = NbtTools.toBlockPos(nbt.getCompound("entityPos"));
		BlockEntity entity = level.getBlockEntity(entityPos);

		NodeType type = NodeType.fromString(nbt.getString("type"));

		return (entity instanceof WireConnectorBlockEntity _entity)
				? new NetworkNode(_entity, type)
				: null;
	}

	public static enum NodeType{
		INPUT("input"),
		OUTPUT("output"),
		IDLE("idle");

		private String name;

		static final Map<String, NodeType> nameMap = new HashMap<>();
		NodeType(String name){
			this.name = name;
		}

		public String asString(){
			return name;
		}

		public static NodeType fromString(String name){
			if(name.isEmpty())
				return NodeType.IDLE;

			return nameMap.get(name);
		}

		static {
			for(NodeType type : NodeType.values()){
				nameMap.put(type.name, type);
			}
		}
	}
}
