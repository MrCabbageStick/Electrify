package mrcabbagestick.electrify;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Electrify implements ModInitializer {
	public static final String MOD_ID = "electrify";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ElectrifyBlocks.registerBlocks();
		ElectrifyItems.registerItems();
		ElectrifyItemGroups.registerItemGroups();
	}

	public static Identifier createIdentifier(String path){
		return new Identifier(MOD_ID, path);
	}
}