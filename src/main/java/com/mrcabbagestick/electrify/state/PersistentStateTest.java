package com.mrcabbagestick.electrify.state;

import com.mrcabbagestick.electrify.Electrify;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class PersistentStateTest extends SavedData {

	public int number = 420;

	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		compoundTag.putInt("number", number);

		return compoundTag;
	}

	public static PersistentStateTest fromCompoundTag(CompoundTag tag){
		var state = new PersistentStateTest();
		state.number = tag.getInt("number");
		return state;
	}

	public static PersistentStateTest getServerState(MinecraftServer server){
		DimensionDataStorage dimensionDataStorage = server.overworld().getDataStorage();

		PersistentStateTest state = dimensionDataStorage.computeIfAbsent(PersistentStateTest::fromCompoundTag, PersistentStateTest::new, Electrify.ID);

		state.setDirty();

		return state;
	}
}
