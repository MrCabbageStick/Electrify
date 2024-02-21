package com.mrcabbagestick.electrify.content.wires;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import com.mrcabbagestick.electrify.tools.NbtTools;

import com.mrcabbagestick.electrify.tools.PlayerMessaging;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;


public class WireItemBase extends Item {
	public WireItemBase(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos clickedPos = context.getClickedPos();
		Level world = context.getLevel();
		BlockEntity clickedEntity = world.getBlockEntity(clickedPos);

		Player player = context.getPlayer();
		ItemStack usedItem = player.getItemInHand(context.getHand());

		if(clickedEntity instanceof WireConnectorBlockEntity clickedConnectorEntity){

			BlockPos currentTarget = getTarget(usedItem);

			if(currentTarget == null){
				// No target selected
				startWire(usedItem, player, clickedPos, world);
				return InteractionResult.SUCCESS;
			}

			if(currentTarget.distSqr(clickedPos) == 0){
				// Target is the same as clicked
				cancelWire(usedItem, player, clickedPos, world, MessageType.WIRE_CANCELLED.getMessage());
				return InteractionResult.SUCCESS;
			}

			if(world.getBlockEntity(currentTarget) instanceof WireConnectorBlockEntity targetBlockEntity) {
				// Wire can be placed
				if(!finishWire(usedItem, player, clickedPos, world, targetBlockEntity, clickedConnectorEntity))
					cancelWire(usedItem, player, clickedPos, world, MessageType.WIRE_ALREADY_EXISTS.getMessage());
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.FAIL;
	}

	private void cancelWire(ItemStack usedItem, Player player, BlockPos clickedPos, Level world, MutableComponent message){
		removeTarget(usedItem);
		PlayerMessaging.displayFailure(player, message);
		world.playSound(null, clickedPos, SoundEvents.LEASH_KNOT_BREAK, SoundSource.BLOCKS, 1f, 1f);
	}

	private void startWire(ItemStack usedItem, Player player, BlockPos clickedPos, Level world){
		setTarget(usedItem, clickedPos);
		PlayerMessaging.displaySuccess(player, MessageType.WIRE_STARTED.getMessage());
		world.playSound(null, clickedPos, SoundEvents.LEASH_KNOT_PLACE, SoundSource.BLOCKS, 1f, 1f);
	}

	private boolean finishWire(ItemStack usedItem, Player player, BlockPos clickedPos, Level world, WireConnectorBlockEntity target, WireConnectorBlockEntity clicked){

		if(!target.connectTo(clickedPos))
			return false;

		clicked.connectionFrom(target.getBlockPos());
		removeTarget(usedItem);
		PlayerMessaging.displaySuccess(player, MessageType.WIRE_FINISHED.getMessage());
		world.playSound(null, clickedPos, SoundEvents.LEASH_KNOT_PLACE, SoundSource.BLOCKS, 1f, 1f);
		return true;
	}

	public static BlockPos getTarget(ItemStack item){
		CompoundTag pos = item.getTagElement("target");
		return (pos == null) ? null : NbtTools.toBlockPos(pos);
	}

	public static void setTarget(ItemStack item, BlockPos pos){
		item.addTagElement("target", NbtTools.from(pos));
	}

	public static void removeTarget(ItemStack item){
		item.removeTagKey("target");
	}

	public static enum MessageType {
		WIRE_STARTED(Component.literal("Wire started")),
		WIRE_FINISHED(Component.literal("Wire placed")),
		WIRE_CANCELLED(Component.literal("Wire cancelled")),
		WIRE_ALREADY_EXISTS(Component.literal("Connection already exists")),;

		private final MutableComponent message;
		MessageType(MutableComponent message){
			this.message = message;
		}

		public MutableComponent getMessage(){
			return message;
		}
	}
}
