package com.mrcabbagestick.electrify.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

public class PlayerMessaging {

	public static final int COLOR_SUCCESS = 0xff94e35b;
	public static final int COLOR_WARNING = 0xffe3a762;
	public static final int COLOR_FAILURE = 0xffe36262;

	public static void displayText(Player player, Component message){
		player.displayClientMessage(message, true);
	}
	public static void displaySuccess(Player player, MutableComponent message){
		displayText(player, message.setStyle(Style.EMPTY.withColor(COLOR_SUCCESS)));
	}
	public static void displayWarning(Player player, MutableComponent message){
		displayText(player, message.setStyle(Style.EMPTY.withColor(COLOR_WARNING)));
	}
	public static void displayFailure(Player player, MutableComponent message){
		displayText(player, message.setStyle(Style.EMPTY.withColor(COLOR_FAILURE)));
	}
}
