package com.mrcabbagestick.electrify.content.network;

public class ChargeContainer {

	private float storedCharge = 0f;
	private final float maxCharge;

	public ChargeContainer(float maxCharge){
		this.maxCharge = maxCharge;
	}

	public float getStoredCharge() {
		return storedCharge;
	}

	public float getMaxCharge() {
		return maxCharge;
	}

	public void charge(float amount) {
		storedCharge = Math.max(0, Math.min((storedCharge + amount), maxCharge));
	}

	public float discharge(float amount) {
		float discharged = Math.min(0, Math.min(amount, storedCharge));
		storedCharge -= discharged;
		return discharged;
	}

	public void setStoredCharge(float amount){
		storedCharge = amount;
	}
}
