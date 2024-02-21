package com.mrcabbagestick.electrify.entries;

import static com.mrcabbagestick.electrify.Electrify.REGISTRATE;

import com.mrcabbagestick.electrify.content.wires.SteelWireItem;
import com.tterrag.registrate.util.entry.ItemEntry;

public class ElectrifyItems {
	public static final ItemEntry<SteelWireItem> STEEL_WIRE = REGISTRATE.item("steel_wire", SteelWireItem::new).register();
	public static void register(){}
}
