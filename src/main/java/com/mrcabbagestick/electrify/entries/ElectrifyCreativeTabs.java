package com.mrcabbagestick.electrify.entries;

import com.mrcabbagestick.electrify.Electrify;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElectrifyCreativeTabs {

	public static final ResourceKey<CreativeModeTab> MAIN_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Electrify.id("main_item_group"));

	public static void register(){
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_TAB,
				FabricItemGroup.builder().icon(() -> new ItemStack(Items.COBWEB)).title(Component.translatable("itemGroup.electrify.main_item_group")).build()
		);
	}
}
