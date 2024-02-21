package com.mrcabbagestick.electrify.content.generators.small_dynamo;

import com.mrcabbagestick.electrify.Electrify;
import com.mrcabbagestick.electrify.content.network.ChargeContainer;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SmallDynamoBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

	protected ChargeContainer container = new ChargeContainer(2137);
	public SmallDynamoBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putFloat("storedCharge", container.getStoredCharge());
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		container.setStoredCharge(compound.getFloat("storedCharge"));
	}

	@Override
	public void tick() {
		super.tick();
		charge(Math.abs(getSpeed()) / 20f);
	}

	public void charge(float amount){
		setChanged();
		container.charge(amount);
	}

	public float discharge(float amount){
		setChanged();
		return container.discharge(amount);
	}

	public ChargeContainer getChargeContainer(){
		return container;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		Lang.text("Electric Stats: ").forGoggles(tooltip);
		Lang.text("Stored Charge: ").style(ChatFormatting.GRAY).forGoggles(tooltip);
		Lang.text(String.format("%.1f", container.getStoredCharge()) + "\uD83D\uDDF2").style(ChatFormatting.AQUA)
				.add(Lang.text(" / " + container.getMaxCharge()).style(ChatFormatting.DARK_GRAY))
				.forGoggles(tooltip, 1);

		Lang.text("").forGoggles(tooltip);

		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		return true;
	}
}
