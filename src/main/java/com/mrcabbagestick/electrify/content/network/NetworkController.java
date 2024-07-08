package com.mrcabbagestick.electrify.content.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkController {

	private static NetworkController instance = null;

	private List<Network> allNetworks = new ArrayList<>();

	private NetworkController(){}

	public static NetworkController getInstance(){
		if(instance == null)
			instance = new NetworkController();

		return instance;
	}

	public List<Network> getNetworks(){
		return allNetworks;
	}

	public void addNetwork(Network network){
		allNetworks.add(network);
	}
}
