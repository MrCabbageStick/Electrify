package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import com.mrcabbagestick.electrify.tools.NbtTools;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NetworkNode {

	public UUID uuid;
	public Network network;
	public Map<UUID, NetworkLink> links = new HashMap<>();

	public List<NetworkNode> cachedConnections = new ArrayList<>();
	public boolean cacheValid = false;

	private NetworkNode(){}

	public static NetworkNode createWithNetwork(){
		var node = new NetworkNode();

		node.uuid = UUID.randomUUID();
		node.network = NetworkController.createNetwork(node);

		return node;
	}

	public CompoundTag save(CompoundTag compoundTag){
		compoundTag.putUUID("uuid", uuid);

		// Links
		ListTag linksTag = new ListTag();
		links.values().stream().map(networkLink -> networkLink.save(new CompoundTag())).forEach(linksTag::add);
		compoundTag.put("links", linksTag);

		return compoundTag;
	}

	@Nullable
	public static NetworkNode fromCompoundTag(CompoundTag compoundTag, Network network){
		NetworkNode node = new NetworkNode();

		// Network
		node.network = network == null ? NetworkController.createNetwork(node) : network;

		// UUID
		if(compoundTag.contains("uuid", IntArrayTag.TAG_INT_ARRAY))
			node.uuid = compoundTag.getUUID("uuid");
		else {
			Electrify.LOGGER.error("CompoundTag for NetworkNode lacks UUID");
			return null;
		}

		// Links
		ListTag linksTag = compoundTag.getList("links", IntArrayTag.TAG_COMPOUND);

		for (int i = 0; i < linksTag.size(); i++) {
			var link = NetworkLink.fromCompoundTag(linksTag.getCompound(i), network);

			if(link != null)
				node.links.put(link.uuid, link);
		}

		return node;
	}

	public boolean unsafeLinkTo(NetworkNode node){
		return true;
	}

	public boolean linkTo(NetworkNode node){

		if(!network.hasNode(node)){
			network.addNode(node, this);
		}

		if(!cacheValid){
			cachedConnections = new ArrayList<>(links.values().stream().map(networkLink -> networkLink.to).toList());
			cacheValid = true;
		}

		if(cachedConnections.contains(node)){
			return false;
		}

		var link = new NetworkLink(network, this, node);
		links.put(link.uuid, link);
		cachedConnections.add(node);

		NetworkController.removeNetwork(node.network.uuid);
		node.network = network;

		return true;
	}

}
