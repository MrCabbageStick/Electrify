package mrcabbagestick.electrify.tools;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ClientGuiTools {

    public static void setOverlayText(Text text){
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(text, false);
    }
}
