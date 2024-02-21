package com.mrcabbagestick.electrify.entries;

import static com.mrcabbagestick.electrify.Electrify.REGISTRATE;

import com.mrcabbagestick.electrify.content.generators.small_dynamo.SmallDynamoBlockEntity;
import com.mrcabbagestick.electrify.content.generators.small_dynamo.SmallDynamoRenderer;
import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorRenderer;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ElectrifyBlockEntities {

	public static final RegistryEntry<BlockEntityType<WireConnectorBlockEntity>> WIRE_CONNECTOR_BLOCK_ENTITY = REGISTRATE
			.blockEntity("wire_connector_block_entity", WireConnectorBlockEntity::new)
			.validBlocks(
					ElectrifyBlocks.LARGE_INSULATOR_BLOCK,
					ElectrifyBlocks.ROLLER_INSULATOR_BLOCK
			)
			.renderer(() -> WireConnectorRenderer::new)
			.register();

	public static final RegistryEntry<BlockEntityType<SmallDynamoBlockEntity>> SMALL_DYNAMO_BLOCK_ENTITY = REGISTRATE
			.blockEntity("small_dynamo_block_entity", SmallDynamoBlockEntity::new)
			.instance(() -> HalfShaftInstance::new)
			.validBlocks(ElectrifyBlocks.SMALL_DYNAMO_BLOCK)
			.renderer(() -> SmallDynamoRenderer::new)
			.register();

	public static void register(){
	}
}
