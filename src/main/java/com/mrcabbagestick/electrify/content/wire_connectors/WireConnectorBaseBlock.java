package com.mrcabbagestick.electrify.content.wire_connectors;

import com.simibubi.create.foundation.block.IBE;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.level.block.state.BlockState;

import org.joml.Vector3f;

public abstract class WireConnectorBaseBlock<C extends WireConnectorBlockEntity> extends Block implements IBE<C> {


	public WireConnectorBaseBlock(Properties properties) {
		super(properties);
	}

	public abstract Vector3f getConnectorOffset(BlockState state);
}
