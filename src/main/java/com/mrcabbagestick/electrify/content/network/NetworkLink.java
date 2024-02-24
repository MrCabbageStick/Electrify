package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import com.mrcabbagestick.electrify.tools.NbtTools;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import oshi.util.tuples.Pair;

public class NetworkLink {
	public Pair<WireConnectorBlockEntity, WireConnectorBlockEntity> ends;

	public NetworkLink(WireConnectorBlockEntity end1, WireConnectorBlockEntity end2){
		ends = new Pair<>(end1, end2);
	}

	public Pair<WireConnectorBlockEntity, WireConnectorBlockEntity> getEnds(){
		return ends;
	}

	public void asCompoundTag(CompoundTag nbt){
		nbt.put("end1", NbtTools.from(ends.getA().getBlockPos()));
		nbt.put("end2", NbtTools.from(ends.getB().getBlockPos()));
	}

	public static NetworkLink fromCompoundTag(CompoundTag nbt, Level level){
		BlockPos end1pos = NbtTools.toBlockPos(nbt.getCompound("end1"));
		BlockPos end2pos = NbtTools.toBlockPos(nbt.getCompound("end2"));

		return (level.getBlockEntity(end1pos) instanceof WireConnectorBlockEntity end1 && level.getBlockEntity(end2pos) instanceof WireConnectorBlockEntity end2)
				? new NetworkLink(end1, end2)
				: null;
	}
}


