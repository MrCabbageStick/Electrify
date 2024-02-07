package mrcabbagestick.electrify;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class ElectrifyItemGroups {
    public static final RegistryKey<ItemGroup> ELECTRIFY_MAIN_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Electrify.createIdentifier("electrify_main_item_group"));

    public static void registerItemGroups(){
        Registry.register(Registries.ITEM_GROUP, ELECTRIFY_MAIN_ITEM_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.electrify.main_item_group"))
                .icon(() -> new ItemStack(Items.COBWEB))
                .entries((displayContext, entries) -> {
                    entries.add(ElectrifyItems.ROLLER_INSULATOR);
                    entries.add(ElectrifyItems.LARGE_INSULATOR);
                    entries.add(ElectrifyItems.STEEL_WIRE);
                })
                .build()
        );
    }

}
