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
		var uuid = UUID.randomUUID();

		networkData.addNetwork(uuid, network);


		return network;
	}

	public static Map<UUID, Network> getAllNetworks(){
		return networkData.getAllNetworks();
	}
}
