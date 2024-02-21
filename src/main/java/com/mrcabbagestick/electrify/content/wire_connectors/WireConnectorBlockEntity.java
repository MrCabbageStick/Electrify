package com.mrcabbagestick.electrify.content.wire_connectors;

import com.mojang.datafixers.types.templates.CompoundList;

import com.mrcabbagestick.electrify.tools.NbtTools;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.fabricmc.fabric.mixin.attachment.BlockEntityUpdateS2CPacketMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WireConnectorBlockEntity extends SmartBlockEntity {

	public Set<BlockPos> connectedFrom = new HashSet<>();
	public Set<BlockPos> connectedTo = new HashSet<>();
	public Set<Vector3f> renderTo = new HashSet<>();

	public WireConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
	}

	@Override
	protected void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);

		ListTag connectedFromList = nbt.getList("connectedFrom", CompoundTag.TAG_COMPOUND);
		ListTag connectedToList = nbt.getList("connectedTo", CompoundTag.TAG_COMPOUND);
		ListTag renderToList = nbt.getList("renderTo", CompoundTag.TAG_COMPOUND);

		connectedFrom = NbtTools.toBlockPosSet(connectedFromList);
		connectedTo = NbtTools.toBlockPosSet(connectedToList);
		renderTo = NbtTools.toFloatVectorSet(renderToList);
	}

	@Override
	public void writeSafe(CompoundTag nbt) {
		super.writeSafe(nbt);
	}

	@Override
	protected void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);

		nbt.put("connectedTo", NbtTools.from(connectedTo));
		nbt.put("connectedFrom", NbtTools.from(connectedFrom));
		nbt.put("renderTo", NbtTools.fromVectorSet(renderTo));
	}

	@Override
	public void remove() {

		for(BlockPos from : connectedFrom){
			if(level.getBlockEntity(from) instanceof WireConnectorBlockEntity fromConnector) {
				fromConnector.connectedTo.remove(from);
				fromConnector.updateRenderPositions();
				fromConnector.setChanged();
			}
		}

		super.remove();
	}

	@Override
	public void destroy() {

		super.destroy();
	}

	public boolean connectionFrom(BlockPos sourcePos) {

		if (sourcePos.distSqr(getBlockPos()) != 0 && connectedFrom.add(sourcePos)){
			this.setChanged();
			return true;
		}

		return false;
	}

	public boolean connectTo(BlockPos targetPos){
		if(targetPos.distSqr(getBlockPos()) != 0 && !connectedFrom.contains(targetPos) && connectedTo.add(targetPos)){
			setChanged();
			updateRenderPositions();
			return true;
		}

		return false;
	}

	public void updateRenderPositions(){
		renderTo.clear();

		for(BlockPos toCheck : connectedTo){

			BlockState stateToCheck = level.getBlockState(toCheck);

			if(stateToCheck.getBlock() instanceof WireConnectorBaseBlock<?> connector){
				BlockPos relativeBlockPos = toCheck.subtract(getBlockPos());
				renderTo.add(new Vector3f(relativeBlockPos.getX(), relativeBlockPos.getY(), relativeBlockPos.getZ())
						.add(connector.getConnectorOffset(stateToCheck))
				);
			}

		}
	}
}
