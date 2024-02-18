package mrcabbagestick.electrify.tools;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class NbtTools {

    public static <T> T nullOr(T something, T fallback){
        return something == null ? fallback : something;
    }

    public static BlockPos toBlockPos(NbtCompound nbt){
        return new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
    }

    public static NbtCompound from(BlockPos pos){
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        return nbt;
    }

    public static NbtList from(List<BlockPos> poses){
        NbtList nbt = new NbtList();
        poses.stream().map(NbtTools::from).forEach(nbt::add);
        return nbt;
    }

    public static ArrayList<BlockPos> toBlockPosList(NbtList nbt){
        ArrayList<BlockPos> list = new ArrayList<>();

        for(NbtElement elem : nbt){
            if(elem.getType() != NbtElement.COMPOUND_TYPE) continue;

            NbtCompound _elem = (NbtCompound) elem;
            list.add(toBlockPos(_elem));
        }

        return list;
    }
}
