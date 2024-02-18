package mrcabbagestick.electrify.content.wire_connectors;

import mrcabbagestick.electrify.Electrify;
import mrcabbagestick.electrify.ElectrifyBlockEntities;
import mrcabbagestick.electrify.tools.NbtTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class WireConnectorBlockEntity extends BlockEntity {

    public ArrayList<BlockPos> connectedFrom = new ArrayList<>();
    public ArrayList<BlockPos> connectedTo = new ArrayList<>();

    public WireConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        NbtList connectedFromList = nbt.getList("connectedFrom", NbtElement.COMPOUND_TYPE);
        NbtList connectedToList = nbt.getList("connectedTo", NbtElement.COMPOUND_TYPE);

        connectedFrom = NbtTools.toBlockPosList(connectedFromList);
        connectedTo = NbtTools.toBlockPosList(connectedToList);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.put("connectedTo", NbtTools.from(connectedTo));
        nbt.put("connectedFrom", NbtTools.from(connectedFrom));

        super.writeNbt(nbt);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void connectTo(BlockPos pos, BlockState state){
        connectedTo.add(pos);
        markDirty();
        world.updateListeners(getPos(), state, state, Block.NOTIFY_LISTENERS);
    }
}
