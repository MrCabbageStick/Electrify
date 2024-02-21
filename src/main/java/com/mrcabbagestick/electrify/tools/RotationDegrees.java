package com.mrcabbagestick.electrify.tools;

public enum RotationDegrees {
	DEG0(0),
	DEG90(90),
	DEG180(180),
	DEG270(270),
	DEG360(360);
	private final int degrees;

	RotationDegrees(int degrees) {
		this.degrees = degrees;
	}

	int asInt() {
		return this.degrees;
	}
}
