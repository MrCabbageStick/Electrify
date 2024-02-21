package com.mrcabbagestick.electrify.entries;

import static com.mrcabbagestick.electrify.Electrify.REGISTRATE;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorRenderer;
import com.tterrag.registrate.util.entry.RegistryEntry;

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

	public static void register(){}
}
