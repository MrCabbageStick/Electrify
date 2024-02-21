package com.mrcabbagestick.electrify;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ElectrifyClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Electrify.LOGGER.info("Hello from the client side");
	}
}
