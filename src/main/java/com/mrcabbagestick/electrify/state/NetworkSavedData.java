package com.mrcabbagestick.electrify.state;

import com.mrcabbagestick.electrify.Electrify;

import com.mrcabbagestick.electrify.content.network.Network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkSavedData extends SavedData {

	private Map<UUID, Network> networks = new HashMap<>();

	@Nullable
	public Network getNetwork(UUID uuid){
		return networks.get(uuid);
	}

	public void addNetwork(UUID uuid, Network network){
		networks.put(uuid, network);
		setDirty();
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		/* Data to be saved */
		var dimensionNetworkList = new ListTag();

		networks.forEach((uuid, network) -> {
			var networkTag = new CompoundTag();
			network.save(networkTag);
			networkTag.putUUID("uuid", uuid);

			dimensionNetworkList.add(networkTag);
		});

		compoundTag.put("Networks", dimensionNetworkList);

//		Electrify.LOGGER.info("Saved network data of size: {}", compoundTag.getList("Networks", Tag.TAG_COMPOUND).size());
		return compoundTag;
	}

	public static NetworkSavedData fromCompoundTag(CompoundTag compoundTag){
		var data = new NetworkSavedData();

//		Electrify.LOGGER.info("Trying to load network data");

		/* Data to be read */
		if(compoundTag.contains("Networks", Tag.TAG_LIST)){

			ListTag dimensionNetworkList = compoundTag.getList("Networks", Tag.TAG_COMPOUND);
			Map<UUID, Network> networks = new HashMap<>();

//			Electrify.LOGGER.info("Found network list of size: {}", dimensionNetworkList.size());

			for (int i = 0; i < dimensionNetworkList.size(); i++) {
				CompoundTag networkTag = dimensionNetworkList.getCompound(i);

				if(!networkTag.contains("uuid"))
					break;

				UUID networkUuid = networkTag.getUUID("uuid");
				Network network = Network.fromCompoundTag(networkTag);

				if(network == null)
					break;

				networks.put(networkUuid, network);
			}

			data.networks = networks;
		}

		return data;
	}

	public Map<UUID, Network> getAllNetworks(){
		return networks;
	}

	public static NetworkSavedData fromServer(MinecraftServer server){

		var dataStorage = server.overworld().getDataStorage();

		var data = dataStorage.computeIfAbsent(NetworkSavedData::fromCompoundTag, NetworkSavedData::new, Electrify.ID);

		data.setDirty();
		return data;
	}

}
