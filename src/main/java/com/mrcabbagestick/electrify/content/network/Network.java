package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.tools.NbtTools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Network {

	public UUID uuid;
	public Map<UUID, NetworkNode> allNodes = new HashMap<>();

	public Network(){
		uuid = UUID.randomUUID();
	}

	public Network(NetworkNode node){
		this();

		allNodes.put(node.uuid, node);
	}

	public CompoundTag save(CompoundTag compoundTag){
		compoundTag.putUUID("uuid", uuid);

		ListTag nodesTag = new ListTag();
		allNodes.values().stream().map(networkNode -> networkNode.save(new CompoundTag())).forEach(nodesTag::add);
		compoundTag.put("nodes", nodesTag);

		Electrify.LOGGER.info(compoundTag.toString());

		return compoundTag;
	}

	public static Network fromCompoundTag(CompoundTag compoundTag){
		var network = new Network();

		// UUID
		if((network.uuid = NbtTools.getUUID("uuid", compoundTag)) == null){
			Electrify.LOGGER.error("CompoundTag for Network lacks UUID");
			return null;
		}

		// Nodes
		ListTag nodesTag = compoundTag.getList("nodes", Tag.TAG_COMPOUND);

		for (int i = 0; i < nodesTag.size(); i++) {
			NetworkNode node = NetworkNode.fromCompoundTag(nodesTag.getCompound(i), network);

			if(node != null)
				network.allNodes.put(node.uuid, node);
		}


		return network;
	}

	public boolean hasNode(NetworkNode node){
		return allNodes.containsKey(node.uuid);
	}

	public boolean addNode(NetworkNode node, NetworkNode existingNode){
		allNodes.put(node.uuid, node);

		if(!existingNode.linkTo(node)){
			allNodes.remove(node.uuid);
			return false;
		}

		return true;
	}
}
