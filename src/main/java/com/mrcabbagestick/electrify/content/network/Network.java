package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.tools.NbtTools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
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

	// Network
	public int getNetworkSize(){
		return allNodes.size();
	}

	public void mergeWith(Network network){
		// Change network in all nodes
		network.allNodes.values().forEach(networkNode -> networkNode.setNetwork(this));

		// Merge nodes
		allNodes.putAll(network.allNodes);

		// Remove network
		NetworkController.removeNetwork(network.getUuid());
	}

	public void walkFrom(UUID nodeUuid){
		if(!allNodes.containsKey(nodeUuid))
			return;

		var startNode = allNodes.get(nodeUuid);

		Stack<NetworkNode> branchingNodes = new Stack<>();
		Set<UUID> checkedNodes = new HashSet<>();

		walkFrom(startNode, branchingNodes, checkedNodes, 0);
	}

	private void walkFrom(NetworkNode startNode, Stack<NetworkNode> branchingNodes, Set<UUID> checkedNodes, int indent){
		var nodeLinks = startNode.getLinks();

		Electrify.LOGGER.info("|".repeat(indent) + "- " + startNode.uuid);

		// Node was checked (for branching purposes)
		if(checkedNodes.contains(startNode.uuid)){
			return;
		}

		checkedNodes.add(startNode.uuid);

		// End node
		if(nodeLinks.isEmpty()){
			return;
		}

		List<NetworkLink> linkList = nodeLinks.values().stream().toList();

		// Single link
		if(startNode.getLinks().size() == 1){
			walkFrom(linkList.get(0).to, branchingNodes, checkedNodes, indent + 1);
			return;
		}

		// Multiple links
		for(var link : linkList){
			walkFrom(link.to, branchingNodes, checkedNodes, indent + 1);
		}
	}

	// Node
	public boolean hasNode(NetworkNode node){
		return allNodes.containsKey(node.uuid);
	}

	public boolean addNode(NetworkNode node, NetworkNode existingNode){
		allNodes.put(node.uuid, node);

		if(!existingNode.unsafeLinkTo(node)){
			allNodes.remove(node.uuid);
			return false;
		}

		return true;
	}

	public NetworkNode getNode(UUID uuid){
		return allNodes.get(uuid);
	}

	// Getters
	public UUID getUuid() {
		return uuid;
	}

	// Compound Tag
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

		// Links
		network.allNodes.values().forEach(NetworkNode::initializeLinks);

		return network;
	}
}
