package mrcabbagestick.electrify.content.wires;

import mrcabbagestick.electrify.Electrify;
import mrcabbagestick.electrify.content.misc.ElectrifyTextStyles;
import mrcabbagestick.electrify.content.wire_connectors.WireConnectorBase;
import mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import mrcabbagestick.electrify.tools.ClientGuiTools;
import mrcabbagestick.electrify.tools.NbtTools;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WireItemBase extends Item {
    public WireItemBase(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos clickedPos = context.getBlockPos();
        World world = context.getWorld();
        BlockEntity clickedEntity = world.getBlockEntity(clickedPos);

        PlayerEntity player = context.getPlayer();
        ItemStack usedWith = player.getStackInHand(context.getHand());

        if(clickedEntity instanceof WireConnectorBlockEntity){

            BlockPos currentTarget = getTarget(usedWith);

            if(currentTarget == null){
                // No target selected
                setTarget(usedWith, clickedPos);
                player.sendMessage(Text.of("Wire started").getWithStyle(ElectrifyTextStyles.COLOR_SUCCESS).get(0), true);
                world.playSound(null, clickedPos, SoundEvents.ENTITY_LEASH_KNOT_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                return ActionResult.SUCCESS;
            }

            if(currentTarget.getSquaredDistance(clickedPos) == 0){
                // Target is the same as clicked
                removeTarget(usedWith);
                player.sendMessage(Text.of("Wire cancelled").getWithStyle(ElectrifyTextStyles.COLOR_FAIL).get(0), true);
                world.playSound(null, clickedPos, SoundEvents.ENTITY_LEASH_KNOT_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                return ActionResult.SUCCESS;
            }

            if(world.getBlockEntity(currentTarget) instanceof WireConnectorBlockEntity targetBlockEntity) {
                // Wire can be placed
                removeTarget(usedWith);
                player.sendMessage(Text.of("Wire placed").getWithStyle(ElectrifyTextStyles.COLOR_SUCCESS).get(0), true);
                world.playSound(null, clickedPos, SoundEvents.ENTITY_LEASH_KNOT_PLACE, SoundCategory.BLOCKS, 1f, 1f);

                targetBlockEntity.connectTo(clickedPos, world.getBlockState(currentTarget));

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL;
    }

    public static BlockPos getTarget(ItemStack item){
        NbtCompound pos = item.getSubNbt("target");
        return (pos == null) ? null : NbtTools.toBlockPos(pos);
    }

    public static void setTarget(ItemStack item, BlockPos pos){
        item.setSubNbt("target", NbtTools.from(pos));
    }

    public static void removeTarget(ItemStack item){
        item.removeSubNbt("target");
    }
}
