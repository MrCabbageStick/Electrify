package com.mrcabbagestick.electrify.entries;

import com.mrcabbagestick.electrify.content.network.Network;
import com.mrcabbagestick.electrify.content.network.NetworkController;

import com.mrcabbagestick.electrify.content.network.NetworkNode;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ElectrifyCommands {

	static NetworkNode addedLast = null;

	public static void register(){
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

			dispatcher.register(Commands.literal("all_networks").executes(context -> {
//				context.getSource().sendSuccess(() -> Component.literal("test response"), false);

				MutableComponent message = MutableComponent.create(Component.literal("All networks:\n").getContents());

				NetworkController.getAllNetworks().forEach((uuid, network) -> {
					message.append(String.format("> Network{uuid: %s, nodes: %d}\n", uuid, network.allNodes.size()));
				});

				context.getSource().sendSuccess(() -> message, false);

				return 1;
			}));

			dispatcher.register(Commands.literal("add_network").executes(context -> {
				addedLast = NetworkNode.createWithNetwork();

				context.getSource().sendSuccess(() -> Component.literal("Network added"), false);
				return 1;
			}));

			dispatcher.register(Commands.literal("clear_networks").executes(context -> {
				NetworkController.clear();

				context.getSource().sendSuccess(() -> Component.literal("Networks cleared"), false);
				addedLast = null;

				return 1;
			}));

			dispatcher.register(Commands.literal("add_link").executes(context -> {

				if(addedLast != null){
					var node = NetworkNode.createWithNetwork();
					addedLast.linkTo(node);

					return 1;
				}

				context.getSource().sendSuccess(() ->
						Component.literal("No network to add a node to"),
						false);
				return 0;
			}));

			dispatcher.register(Commands.literal("walk_network").executes(context -> {

				var network = addedLast.network;

				context.getSource().sendSuccess(() ->
								Component.literal("No network to add a node to"),
						false);
				return 0;
			}));

		});
	}
}
