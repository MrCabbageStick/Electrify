package mrcabbagestick.electrify.content.wires;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum WireTypes {
    STEEL_WIRE("steel_wire", 0xff1c1816, 0xff302b26),
    ;

    public static final float SAG_FACTOR = .99f;
    public static final float WIRE_LENGTH = 10f;
    public static final int SEGMENTS_PER_BLOCK = 3;
    public static final float HALF_WIRE_WIDTH = .025f;

    private static Map<String, WireTypes> nameWireMap = new HashMap<>();
    private final List<Integer> COLORS;
    private final String NAME;

    WireTypes(String name, int color1, int color2){
        NAME = name;
        COLORS = List.of(color1, color2);
    }

    public List<Integer> getColors(){
        return COLORS;
    }
    public String toString(){ return NAME; }
    public static WireTypes fromString(String name){
        return nameWireMap.get(name);
    }

    public static float centenary(float segmentIndex, float entireLength, int segmentCount) {
        float a = entireLength / WIRE_LENGTH * SAG_FACTOR;
        segmentIndex = (segmentIndex / segmentCount * 2 - 1); // (segmentIndex / sections) = <0, 1> -> *2 -> <0, 2> -> -1 -> < -1, 1>
        return (float) ((Math.cosh(segmentIndex) - Math.cosh(1.0f)) * a);
    }

    static {
        for(WireTypes type : WireTypes.values()){
            nameWireMap.put(type.toString(), type);
        }
    }
}
