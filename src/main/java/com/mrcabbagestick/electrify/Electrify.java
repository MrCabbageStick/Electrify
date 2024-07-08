package com.mrcabbagestick.electrify;

import com.mrcabbagestick.electrify.content.network.NetworkController;
import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import com.mrcabbagestick.electrify.entries.ElectrifyCommands;
import com.mrcabbagestick.electrify.state.PersistentStateTest;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.world.level.levelgen.WorldDimensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mrcabbagestick.electrify.entries.ElectrifyBlockEntities;
import com.mrcabbagestick.electrify.entries.ElectrifyBlocks;
import com.mrcabbagestick.electrify.entries.ElectrifyCreativeTabs;
import com.mrcabbagestick.electrify.entries.ElectrifyItems;
import com.simibubi.create.foundation.data.CreateRegistrate;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class Electrify implements ModInitializer {
	public static final String ID = "electrify";
	public static final String NAME = "Electrify";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID).defaultCreativeTab(ElectrifyCreativeTabs.MAIN_TAB);

	@Override
	public void onInitialize() {
		ElectrifyCreativeTabs.register();
		ElectrifyItems.register();
		ElectrifyBlockEntities.register();
		ElectrifyBlocks.register();

		ElectrifyCommands.register();

		REGISTRATE.register();

		ServerWorldEvents.LOAD.register((server, world) -> {
			if(world.dimension().equals(server.overworld().dimension())){
				NetworkController.loadNetworkData(server);
			}
		});
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}
