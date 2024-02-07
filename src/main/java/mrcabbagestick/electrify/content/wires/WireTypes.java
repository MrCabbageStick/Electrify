package mrcabbagestick.electrify.content.wires;

import java.util.List;

public enum WireTypes {
    STEEL_WIRE(0xff1c1816, 0xff302b26),
    ;

    public static final float SAG_FACTOR = .99f;
    public static final float WIRE_LENGTH = 10f;
    public static final int SEGMENTS_PER_BLOCK = 3;
    public static final float HALF_WIRE_WIDTH = .025f;
    private final List<Integer> COLORS;

    WireTypes(int color1, int color2){
        COLORS = List.of(color1, color2);
    }

    public List<Integer> getColors(){
        return COLORS;
    }

    public static float centenary(float segmentIndex, float entireLength, int segmentCount) {
        float a = entireLength / WIRE_LENGTH * SAG_FACTOR;
        segmentIndex = (segmentIndex / segmentCount * 2 - 1); // (segmentIndex / sections) = <0, 1> -> *2 -> <0, 2> -> -1 -> < -1, 1>
        return (float) ((Math.cosh(segmentIndex) - Math.cosh(1.0f)) * a);
    }
}
