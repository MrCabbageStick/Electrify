package com.mrcabbagestick.electrify;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

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

		REGISTRATE.register();

		ServerWorldEvents.LOAD.register((server, world) -> {
//			world.getAllEntities().forEach();
		});
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}
