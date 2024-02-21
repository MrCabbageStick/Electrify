package com.mrcabbagestick.electrify.entries;

import static com.mrcabbagestick.electrify.Electrify.REGISTRATE;

import com.mrcabbagestick.electrify.content.dynamos.SmallDynamoBlock;
import com.mrcabbagestick.electrify.content.wire_connectors.insulators.LargeInsulatorBlock;
import com.mrcabbagestick.electrify.content.wire_connectors.insulators.RollerInsulator;
import com.tterrag.registrate.util.entry.BlockEntry;

public class ElectrifyBlocks {
	public static final BlockEntry<LargeInsulatorBlock> LARGE_INSULATOR_BLOCK = REGISTRATE.block("large_insulator", LargeInsulatorBlock::new).simpleItem().register();
	public static final BlockEntry<RollerInsulator> ROLLER_INSULATOR_BLOCK = REGISTRATE.block("roller_insulator", RollerInsulator::new).simpleItem().register();
	public static final BlockEntry<SmallDynamoBlock> SMALL_DYNAMO_BLOCK = REGISTRATE.block("small_dynamo", SmallDynamoBlock::new).simpleItem().register();
	public static void register(){}
}
