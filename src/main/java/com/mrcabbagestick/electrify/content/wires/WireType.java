package com.mrcabbagestick.electrify.content.wires;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum WireType {
	STEEL_WIRE("steel_wire", 0xff1c1816, 0xff302b26),
	;

	public static final float SAG_FACTOR = .99f;
	public static final float WIRE_LENGTH = 10f;
	public static final int SEGMENTS_PER_BLOCK = 3;
	public static final float HALF_WIRE_WIDTH = .025f;

	private static Map<String, WireType> nameWireMap = new HashMap<>();
	private final List<Integer> COLORS;
	private final String NAME;

	WireType(String name, int color1, int color2){
		NAME = name;
		COLORS = List.of(color1, color2);
	}

	public List<Integer> getColors(){
		return COLORS;
	}
	public String toString(){ return NAME; }
	public static WireType fromString(String name){
		return nameWireMap.get(name);
	}

	static {
		for(WireType type : WireType.values()){
			nameWireMap.put(type.toString(), type);
		}
	}
}
