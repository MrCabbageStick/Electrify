package com.mrcabbagestick.electrify.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagTypes;

import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NbtTools {

	public static BlockPos toBlockPos(CompoundTag nbt){
		return new BlockPos(
				nbt.getInt("x"),
				nbt.getInt("y"),
				nbt.getInt("z")
		);
	}

	public static CompoundTag from(BlockPos pos){
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("x", pos.getX());
		nbt.putInt("y", pos.getY());
		nbt.putInt("z", pos.getZ());
		return nbt;
	}

	public static Set<BlockPos> toBlockPosSet(ListTag nbt){
		Set<BlockPos> blockPosList = new HashSet<>(nbt.size());

		for(Tag elem : nbt){
			if(elem.getType() != TagTypes.getType(Tag.TAG_COMPOUND)) continue;

			blockPosList.add(toBlockPos((CompoundTag) elem));
		}

		return blockPosList;
	}

	public static ListTag from(Set<BlockPos> poses){
		ListTag nbt = new ListTag();
		poses.stream().map(NbtTools::from).forEach(nbt::add);
		return nbt;
	}

	public static Vector3f toVector3f(CompoundTag nbt){
		return new Vector3f(
				nbt.getFloat("x"),
				nbt.getFloat("y"),
				nbt.getFloat("z")
		);
	}

	public static CompoundTag from(Vector3f pos){
		CompoundTag nbt = new CompoundTag();
		nbt.putFloat("x", pos.x);
		nbt.putFloat("y", pos.y);
		nbt.putFloat("z", pos.z);
		return nbt;
	}

	public static Set<Vector3f> toFloatVectorSet(ListTag nbt){
		Set<Vector3f> list = new HashSet<>();

		for(Tag elem : nbt){
			if(elem.getType() != TagTypes.getType(Tag.TAG_COMPOUND)) continue;
			list.add(toVector3f((CompoundTag) elem));
		}

		return list;
	}

	public static ListTag fromVectorSet(Set<Vector3f> array){
		ListTag nbt = new ListTag();
		array.stream().map(NbtTools::from).forEach(nbt::add);
		return nbt;
	}

	public static UUID getUUID(String key, CompoundTag compoundTag){
		if(!compoundTag.contains(key, Tag.TAG_INT_ARRAY))
			return null;

		return compoundTag.getUUID(key);
	}
}
