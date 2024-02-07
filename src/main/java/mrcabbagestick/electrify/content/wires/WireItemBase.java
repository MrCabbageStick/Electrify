package mrcabbagestick.electrify.content.wires;

import mrcabbagestick.electrify.ElectrifyBlockTags;
import mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WireItemBase extends Item {

    private BlockPos savedPosition = null;
    public WireItemBase(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        BlockPos clickedPos = context.getBlockPos();
        BlockState clickedBlock = world.getBlockState(clickedPos);
        PlayerEntity player = context.getPlayer();

        if(world.isClient() || !clickedBlock.isIn(ElectrifyBlockTags.WIRE_CONNECTORS))
            return ActionResult.FAIL;

        if(savedPosition == null){
            player.sendMessage(Text.of("Wire from: " + clickedPos.toString()));
            savedPosition = clickedPos;
            world.playSoundAtBlockCenter(clickedPos, SoundEvents.ENTITY_LEASH_KNOT_PLACE, SoundCategory.BLOCKS, 1f, .5f, false);
            return ActionResult.SUCCESS;
        }

        if(savedPosition == clickedPos){
            player.sendMessage(Text.of("Wire cancelled"));
            savedPosition = null;
            world.playSoundAtBlockCenter(clickedPos, SoundEvents.ENTITY_LEASH_KNOT_BREAK, SoundCategory.BLOCKS, 1f, .5f, false);
            return ActionResult.SUCCESS;
        }


        ((WireConnectorBlockEntity) world.getBlockEntity(savedPosition)).connectTo(clickedPos);

        world.playSoundAtBlockCenter(clickedPos, SoundEvents.ENTITY_LEASH_KNOT_PLACE, SoundCategory.BLOCKS, 1f, .5f, false);

        player.getStackInHand(context.getHand()).decrement(1);

        savedPosition = null;
        return ActionResult.SUCCESS;
    }
}
