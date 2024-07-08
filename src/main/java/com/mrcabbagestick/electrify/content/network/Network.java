package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.Electrify;
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

	public static Network fromCompoundTag(CompoundTag compoundTag){
		return new Network();
	}

	public CompoundTag save(CompoundTag compoundTag){
		compoundTag.putString("temp_string", "Network");

		return compoundTag;
	}
}
