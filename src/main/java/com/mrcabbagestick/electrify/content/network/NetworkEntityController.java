package com.mrcabbagestick.electrify.content.network;

import com.mrcabbagestick.electrify.content.wire_connectors.WireConnectorBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class NetworkEntityController {

	private static Map<WireConnectorBlockEntity, NetworkNode> entityNodeMap = new HashMap<>();

	public static NetworkNode getNode(WireConnectorBlockEntity connector){

		if(!entityNodeMap.containsKey(connector)){
			var node = NetworkNode.createWithNetwork();
			entityNodeMap.put(connector, node);
			return node;
		}

		return entityNodeMap.get(connector);
	}
}
