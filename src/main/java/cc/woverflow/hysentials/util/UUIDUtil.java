package cc.woverflow.hysentials.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.UUID;

public class UUIDUtil {

    public static String getUUIDWithoutDashes() {
        return getClientUUID().toString().toLowerCase().replace("-", "");
    }

    public static UUID getClientUUID() {
        GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
        if (profile != null) {
            UUID id = profile.getId();
            if (id != null) return id;
        }

        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer != null) return thePlayer.getUniqueID();
        return null;
    }
}
