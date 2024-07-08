package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.state.NetworkSavedData;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NetworkController {

	private static NetworkSavedData networkData = null;

	public static void loadNetworkData(MinecraftServer server){
		networkData = NetworkSavedData.fromServer(server);
		Electrify.LOGGER.info("Loaded saved network data");
	}

	public static Network getNetwork(UUID uuid){
		return networkData.getNetwork(uuid);
	}

	public static Network createNetwork(){
		var network = new Network();

		networkData.addNetwork(network.uuid, network);

		return network;
	}

	public static Network createNetwork(NetworkNode networkNode){
		var network = new Network(networkNode);

		networkData.addNetwork(network.uuid, network);

		return network;
	}

	public static Network removeNetwork(UUID uuid){
		return networkData.getAllNetworks().remove(uuid);
	}

	public static void clear(){
		networkData.clear();
	}

	public static Map<UUID, Network> getAllNetworks(){
		return networkData.getAllNetworks();
	}
}
