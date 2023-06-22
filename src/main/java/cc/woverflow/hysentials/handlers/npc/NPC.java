package cc.woverflow.hysentials.handlers.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.World;

public class NPC extends EntityOtherPlayerMP{
    public GameProfile profile;

    public NPC(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

//    public NPC(String name, ) {
//        super(world, gameProfile);
//    }

}
