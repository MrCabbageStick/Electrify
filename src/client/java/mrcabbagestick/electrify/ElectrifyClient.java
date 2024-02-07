package mrcabbagestick.electrify;

import net.fabricmc.api.ClientModInitializer;

public class ElectrifyClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ElectrifyBlockEntityRenderers.registerElectrifyBlockEntityRenderers();
	}
}