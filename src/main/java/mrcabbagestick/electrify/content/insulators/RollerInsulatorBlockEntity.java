package mrcabbagestick.electrify.content.insulators;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import mrcabbagestick.electrify.Electrify;
import mrcabbagestick.electrify.ElectrifyBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RollerInsulatorBlockEntity extends BlockEntity {

    public ArrayList<BlockPos> connectedFrom = new ArrayList<>();
    public ArrayList<BlockPos> connectedTo = new ArrayList<>();

//    public ArrayList<Vector3f> connectedFromOffset = new ArrayList<>();
//    public ArrayList<Vector3f> connectedToOffset = new ArrayList<>();

    public RollerInsulatorBlockEntity(BlockPos pos, BlockState state) {
        super(ElectrifyBlockEntities.ROLLER_INSULATOR_BLOCK_ENTITY, pos, state);
    }


    private ArrayList<BlockPos> nbtCompoundListToVectorList(NbtList nbtList){
        if(nbtList.isEmpty()) return new ArrayList<>();

        ArrayList<BlockPos> output = new ArrayList<>();

        for(NbtElement entry : nbtList){
            if(entry.getType() != NbtElement.COMPOUND_TYPE) continue;

            NbtCompound compound = (NbtCompound) entry;

            output.add(new BlockPos(
                compound.getInt("x"),
                compound.getInt("y"),
                compound.getInt("z")
            ));
        }

        return output;
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

//        Electrify.LOGGER.info("Executed: read");
        NbtList connectedFromList = nbt.getList("connectedFrom", NbtElement.COMPOUND_TYPE);
        NbtList connectedToList = nbt.getList("connectedTo", NbtElement.COMPOUND_TYPE);

        connectedFrom = nbtCompoundListToVectorList(connectedFromList);
        connectedTo = nbtCompoundListToVectorList(connectedToList);

//        Electrify.LOGGER.info("Connected to: " + connectedTo.toString());
//        Electrify.LOGGER.info("Connected from: " + connectedFrom.toString());
    }

    private NbtList vectorListToNbtCompoundList(ArrayList<BlockPos> vectorList){
        NbtList output = new NbtList();

        vectorList.stream().map(pos -> {
            NbtCompound compound = new NbtCompound();
            compound.putInt("x", pos.getX());
            compound.putInt("y", pos.getY());
            compound.putInt("z", pos.getZ());
            return compound;
        }).forEach(output::add);

        return output;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

//        Electrify.LOGGER.info("Executed: write");
//        if(!connectedTo.isEmpty())
        nbt.put("connectedTo", vectorListToNbtCompoundList(connectedTo));

//        if(!connectedFrom.isEmpty())
        nbt.put("connectedFrom", vectorListToNbtCompoundList(connectedFrom));

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
}

