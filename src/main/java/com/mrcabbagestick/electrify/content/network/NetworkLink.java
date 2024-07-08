package com.mrcabbagestick.electrify.content.network;

import com.mojang.datafixers.util.Function3;
import com.mrcabbagestick.electrify.Electrify;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class NetworkLink {

	public UUID uuid;
	public Network network;
	public NetworkNode from;
	public NetworkNode to;

	private NetworkLink(){};

	public NetworkLink(@NotNull Network network, @NotNull NetworkNode from, @NotNull NetworkNode to) {
		this.network = network;
		this.from = from;
		this.to = to;

		uuid = UUID.randomUUID();
	}

	public CompoundTag save(CompoundTag compoundTag){
		compoundTag.putUUID("uuid", uuid);

		if(to != null)
			compoundTag.putUUID("connectedTo", to.uuid);
		else
			Electrify.LOGGER.error("Network link lacks the end Node");

		return compoundTag;
	}

	public static NetworkLink fromCompoundTag(CompoundTag compoundTag, Network network){
		NetworkLink link = new NetworkLink();

		// Network
		link.network = network;

		// UUID
		if(compoundTag.contains("uuid", IntArrayTag.TAG_INT_ARRAY))
			link.uuid = compoundTag.getUUID("uuid");
		else {
			Electrify.LOGGER.error("CompoundTag for NetworkLink lacks UUID");
			return null;
		}

		// End network node
		if(compoundTag.contains("connectedTo", IntArrayTag.TAG_INT_ARRAY))
			link.uuid = compoundTag.getUUID("connectedTo");
		else {
			Electrify.LOGGER.error("CompoundTag for NetworkLink lacks end Node");
			return null;
		}

		return link;
	}
}


