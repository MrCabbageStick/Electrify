package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import com.mrcabbagestick.electrify.content.wires.WireType;
import com.mrcabbagestick.electrify.tools.NbtTools;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import oshi.util.tuples.Pair;

public class NetworkLink {
	private WireType wireType;

	public NetworkLink(WireType type){
		wireType = type;
	}

	public WireType getWireType() {
		return wireType;
	}

	public void setWireType(WireType wireType) {
		this.wireType = wireType;
	}

	// ADDITIONAL
	public void writeToCompoundTag(CompoundTag nbt){
		nbt.putString("type", wireType.toString());
	}

	public static NetworkLink fromCompoundTag(CompoundTag nbt){
		return new NetworkLink(
				WireType.fromString(nbt.getString("type"))
		);
	}
}


