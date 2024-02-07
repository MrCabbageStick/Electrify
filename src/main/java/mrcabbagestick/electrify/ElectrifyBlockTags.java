package mrcabbagestick.electrify;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ElectrifyBlockTags {
    public static final TagKey<Block> WIRE_CONNECTORS = TagKey.of(RegistryKeys.BLOCK, Electrify.createIdentifier("wire_connectors"));

}
