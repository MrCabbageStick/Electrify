package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Network {

	private HashMap<UUID, NetworkNode> allNodes = new HashMap<>();
	private Set<WireConnectorBlockEntity> allNodeEntities = new HashSet<>();
	private Set<UUID> allSources = new HashSet<>();
	private Set<UUID> allConsumers = new HashSet<>();
	private HashMap<UUID, NetworkLink> allLinks = new HashMap<>();

	private float lastConsumed = 0;
	private float lastGenerated = 0;

	public Network() {
	}

	public float getLastConsumed() {
		return lastConsumed;
	}
	public float getLastGenerated() {
		return lastGenerated;
	}

	public boolean addNode(WireConnectorBlockEntity connectorEntity){

		if(!allNodeEntities.add(connectorEntity))
			return false;

		UUID uuid = UUID.randomUUID();
		connectorEntity.networkNodeUuid = uuid;

		allNodes.put(uuid, new NetworkNode(connectorEntity, NetworkNode.NodeType.IDLE));
		return true;
	}

	public boolean removeNode(UUID nodeUuid){
		NetworkNode node = allNodes.get(nodeUuid);

		if(node == null)
			return false;

		allNodeEntities.remove(node.getConnectorEntity());
		allNodes.remove(nodeUuid);

		return true;
	}

	public void mergeWith(Network network){
		this.allNodes.putAll(network.allNodes);
		this.allLinks.putAll(network.allLinks);
		allSources.addAll(network.allSources);
		allConsumers.addAll(network.allConsumers);
	}

	public CompoundTag asCompoundTag(){
		CompoundTag nbt = new CompoundTag();

		ListTag nodes = new ListTag();
		allNodes.forEach((uuid, networkNode) -> {
			CompoundTag nodeTag = new CompoundTag();
			nodeTag.putString("uuid", uuid.toString());
			networkNode.asCompoundTag(nodeTag);
			nodes.add(nodeTag);
		});
		nbt.put("nodes", nodes);

		ListTag links = new ListTag();
		allLinks.forEach( (uuid, networkLink)-> {
			CompoundTag nodeTag = new CompoundTag();
			nodeTag.putString("uuid", uuid.toString());
			networkLink.asCompoundTag(nodeTag);
			nodes.add(nodeTag);
		});
		nbt.put("links", links);

		ListTag sources = new ListTag();
		sources.addAll(allSources.stream().map(uuid -> StringTag.valueOf(uuid.toString())).toList());
		nbt.put("sources", sources);

		ListTag consumers = new ListTag();
		consumers.addAll(allConsumers.stream().map(uuid -> StringTag.valueOf(uuid.toString())).toList());
		nbt.put("consumers", consumers);

		return nbt;
	}

	public static Network fromCompoundTag(CompoundTag nbt, Level level){
		Network network = new Network();

		Tag nodes = nbt.get("nodes");
		if(nodes.getType() == ListTag.TYPE){
			for(Tag _tag : ((ListTag) nodes)){
				if(_tag.getType() != CompoundTag.TYPE) continue;

				CompoundTag tag = (CompoundTag) _tag;

				String uuidString = tag.getString("uuid");
				if(uuidString.isEmpty()) continue;

				NetworkNode node = NetworkNode.fromCompoundTag(tag, level);
				network.allNodes.put(UUID.fromString(uuidString), node);
			}
		}

		Tag links = nbt.get("links");
		if(links.getType() == ListTag.TYPE){
			for(Tag _tag : ((ListTag) links)){
				if(_tag.getType() != CompoundTag.TYPE) continue;

				CompoundTag tag = (CompoundTag) _tag;

				String uuidString = tag.getString("uuid");
				if(uuidString.isEmpty()) continue;

				NetworkLink link = NetworkLink.fromCompoundTag(tag, level);
				network.allLinks.put(UUID.fromString(uuidString), link);
			}
		}

		network.allSources = nbt.getList("sources", Tag.TAG_STRING)
				.stream().map(tag -> UUID.fromString(tag.toString()))
				.collect(Collectors.toSet());

		network.allConsumers = nbt.getList("consumers", Tag.TAG_STRING)
				.stream().map(tag -> UUID.fromString(tag.toString()))
				.collect(Collectors.toSet());

		return network;
	}
}
