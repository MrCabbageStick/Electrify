package com.mrcabbagestick.electrify.content.network;


import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import com.mrcabbagestick.electrify.content.wires.WireType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Network {
	public HashMap<UUID, NetworkNode> allNodes = new HashMap<>();
	public HashMap<UUID, NetworkNode.UninitializedNetworkNode> allUninitializedNodes = new HashMap<>();

	public HashMap<Pair<UUID, UUID>, NetworkLink> allLinks = new HashMap<>(); // Keys contain pairs and reversed pairs
	public Set<UUID> allGenerators = new HashSet<>();
	public Set<UUID> allConsumers = new HashSet<>();
	public UUID networkController = null;

	public Network(){}
	public Network(WireConnectorBlockEntity firstNode){
		allNodes.put(firstNode.networkNodeUuid, new NetworkNode(firstNode));
		networkController = firstNode.networkNodeUuid;
	}


	// NODE stuff
	public void addNode(WireConnectorBlockEntity entity){
		allNodes.put(entity.networkNodeUuid, new NetworkNode(entity));
	}

	public void removeNode(UUID nodeUuid){
		// No node -> do nothing
		if(!allNodes.containsKey(nodeUuid))
			return;

		// Remove link if nodeUuid is in pair
		allLinks.keySet().forEach(key -> {
			if(key.getA() == nodeUuid || key.getB() == nodeUuid)
				removeInnerLink(key.getA(), key.getB());
		});
	}

	/// LINK stuff
	public LinkAddResponse addLink(WireConnectorBlockEntity end1, WireConnectorBlockEntity end2, WireType wireType){

		// Nodes are the same
		if(end1.networkNodeUuid == end2.networkNodeUuid){
			return LinkAddResponse.CANT_LINK_THE_SAME_NODE;
		}

		// Connection exists
		if(allLinks.containsKey(new Pair<>(end1.networkNodeUuid, end2.networkNodeUuid))){
			return LinkAddResponse.LINK_ALREADY_EXISTS;
		}

		boolean end1inNodes = allNodes.containsKey(end1.networkNodeUuid);
		boolean end2inNodes = allNodes.containsKey(end2.networkNodeUuid);

		// Both nodes are in this network
		if(end1inNodes && end2inNodes){
			createInnerLink(end1.networkNodeUuid, end2.networkNodeUuid, wireType);
			return LinkAddResponse.NO_MERGE;
		}

		// One node is in this network
		WireConnectorBlockEntity thisNetworkNode, otherNetworkNode;
		if(allNodes.containsKey(end1.networkNodeUuid)){
			thisNetworkNode = end1;
			otherNetworkNode = end2;
		}
		else{
			thisNetworkNode = end2;
			otherNetworkNode = end1;
		}

		// Find bigger network and merge smaller to it
		// This network is bigger or of a same size
		if(this.getNetworkSize() >= otherNetworkNode.network.getNetworkSize()){

			this.mergeWith(otherNetworkNode.network);
			createInnerLink(thisNetworkNode.networkNodeUuid, otherNetworkNode.networkNodeUuid, wireType);
			return LinkAddResponse.NETWORKS_MERGED;
		}
		// Other network is bigger
		else{
			otherNetworkNode.network.mergeWith(this);
			otherNetworkNode.network.createInnerLink(thisNetworkNode.networkNodeUuid, otherNetworkNode.networkNodeUuid, wireType);
			return LinkAddResponse.NETWORKS_MERGED_REVERSED;
		}
	}

	public boolean changeLinkType(UUID end1, UUID end2, WireType newType){
		Pair<UUID, UUID> allLinksKey = new Pair<>(end1, end2);

		// No such link
		if(!allLinks.containsKey(allLinksKey))
			return false;

		allLinks.get(allLinksKey).setWireType(newType);
		return true;
	}

	/**
	 * Use ONLY when you are sure both ends are in the field: allNodes
	 */
	private void createInnerLink(UUID end1, UUID end2, WireType wireType){
		createInnerLink(end1, end2, new NetworkLink(wireType));

	}

	private void createInnerLink(UUID end1, UUID end2, NetworkLink link){
		Pair<UUID, UUID> allLinksKey = new Pair<>(end1, end2);
		Pair<UUID, UUID> allLinksKeyReversed = new Pair<>(allLinksKey.getB(), allLinksKey.getA());

		// Make both keys point to the same link
		allLinks.put(allLinksKey, link);
		allLinks.put(allLinksKeyReversed, link);
	}

	/**
	 * Use ONLY when you are sure both ends are in the field: allNodes
	 */
	private void removeInnerLink(UUID end1, UUID end2){

		// TODO: Divide network if no connection left

		Pair<UUID, UUID> allLinksKey = new Pair<>(end1, end2);
		Pair<UUID, UUID> allLinksKeyReversed = new Pair<>(allLinksKey.getB(), allLinksKey.getA());

		allLinks.remove(allLinksKey);
		allLinks.remove(allLinksKeyReversed);
	}

	// NETWORK stuff
	public void mergeWith(Network network){
		allNodes.putAll(network.allNodes);
		allLinks.putAll(network.allLinks);
		allGenerators.addAll(network.allGenerators);
		allConsumers.addAll(network.allConsumers);
	}

	public int getNetworkSize(){
		return allLinks.size() + allNodes.size();
	}


	public static enum LinkAddResponse{
		LINK_ALREADY_EXISTS,
		CANT_LINK_THE_SAME_NODE,
		NO_MERGE,
		NETWORKS_MERGED,
		NETWORKS_MERGED_REVERSED,;
	}

	// ADDITIONAL
	public void writeToCompoundTag(CompoundTag networkNbt){

		// NODES
		ListTag nodes = new ListTag();
		allNodes.forEach((uuid, networkNode) -> {
			CompoundTag nodeTag = new CompoundTag();
			nodeTag.putUUID("uuid", uuid);
			networkNode.writeToCompoundTag(nodeTag);
			nodes.add(nodeTag);
		});
		allUninitializedNodes.forEach((uuid, networkNode) -> {
			CompoundTag nodeTag = new CompoundTag();
			nodeTag.putUUID("uuid", uuid);
			networkNode.writeToCompoundTag(nodeTag);
			nodes.add(nodeTag);
		});
		networkNbt.put("nodes", nodes);

		// LINKS
		ListTag links = new ListTag();
		allLinks.forEach((uuidPair, networkLink) -> {
			CompoundTag linkTag = new CompoundTag();
			linkTag.putUUID("end1", uuidPair.getA());
			linkTag.putUUID("end2", uuidPair.getB());
			networkLink.writeToCompoundTag(linkTag);
			links.add(linkTag);
		});
		networkNbt.put("links", links);

		// CONSUMERS
		ListTag consumers = new ListTag();
		consumers.addAll(allConsumers.stream().map(NbtUtils::createUUID).toList());
		networkNbt.put("consumers", consumers);

		// GENERATORS
		ListTag generators = new ListTag();
		generators.addAll(allGenerators.stream().map(NbtUtils::createUUID).toList());
		networkNbt.put("generators", generators);

		// NETWORK CONTROLLER
		networkNbt.putUUID("networkController", networkController);
	}

	public static Network readFromCompoundTag(CompoundTag networkTag, Level world){
		Network network = new Network();

		// NETWORK CONTROLLER
		network.networkController = networkTag.getUUID("networkController");

		// NODES
		for(Tag _node : networkTag.getList("nodes", Tag.TAG_COMPOUND)){
			if(_node instanceof CompoundTag nodeTag){
				if(world == null){
					NetworkNode.UninitializedNetworkNode node = NetworkNode.UninitializedNetworkNode.readFromCompoundTag(nodeTag);
					UUID uuid = nodeTag.getUUID("uuid");
					network.allUninitializedNodes.put(uuid, node);
				}
				else{
					NetworkNode node = NetworkNode.readFromCompoundTag(nodeTag, world);
					if(node != null) {
						UUID uuid = nodeTag.getUUID("uuid");
						network.allNodes.put(uuid, node);
					}
				}
			}
		}

		// LINKS
		for(Tag _link : networkTag.getList("links", Tag.TAG_COMPOUND)){
			if(_link instanceof CompoundTag linkTag){

				UUID end1 = linkTag.getUUID("end1");
				UUID end2 = linkTag.getUUID("end2");
				NetworkLink link = NetworkLink.fromCompoundTag(linkTag);

				network.createInnerLink(end1, end2, link);
			}
		}

		return network;
	}


	public void initializeNodes(@Nonnull Level world){

		if(isInitialized()) return;

		HashMap<UUID, NetworkNode.UninitializedNetworkNode> stillNotInitialized = new HashMap<>();

		allUninitializedNodes.forEach((uuid, node) -> {
			NetworkNode initialized = node.initialize(world);
			if(initialized != null){
				allNodes.put(uuid, initialized);
			}
			else{
				stillNotInitialized.put(uuid, node);
			}
		});

		allNodes.forEach((uuid, node) -> {
			WireConnectorBlockEntity connector = node.connectorEntity;
			connector.network = this;
			connector.setChanged();
			world.sendBlockUpdated(connector.getBlockPos(), connector.getBlockState(), connector.getBlockState(), Block.UPDATE_NEIGHBORS);
		});

		allUninitializedNodes = stillNotInitialized;
	}

	public boolean isInitialized(){
		return allUninitializedNodes.isEmpty();
	}

	public boolean isNetworkController(UUID uuid){
		return networkController != null && networkController.equals(uuid);
	}
}
