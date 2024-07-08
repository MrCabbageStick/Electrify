package com.mrcabbagestick.electrify.entries;

import com.mojang.brigadier.Command;

import com.mrcabbagestick.electrify.content.network.NetworkController;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;

import java.awt.*;
import java.util.function.Supplier;

public class ElectrifyCommands {

	public static void register(){
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

			dispatcher.register(Commands.literal("all_networks").executes(context -> {
//				context.getSource().sendSuccess(() -> Component.literal("test response"), false);

				MutableComponent message = MutableComponent.create(Component.literal("All networks:\n").getContents());

				NetworkController.getAllNetworks().forEach((uuid, network) -> {
					message.append("> " + uuid.toString() + " -> Network\n");
				});

				context.getSource().sendSuccess(() -> message, false);

				return 1;
			}));

			dispatcher.register(Commands.literal("add_network").executes(context -> {
				NetworkController.createNetwork();
				context.getSource().sendSuccess(() -> Component.literal("Network added"), false);
				return 1;
			}));
		});
	}
}
