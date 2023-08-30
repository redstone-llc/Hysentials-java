package cc.woverflow.hysentials.cosmetics.cubit;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.cosmetics.AbstractCosmetic;
import cc.woverflow.hysentials.util.BUtils;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CubitCompanion extends AbstractCosmetic<EntityCubit> {
    private Map<UUID, EntityCubit> cubits = new HashMap<>();

    public CubitCompanion() {
        super(false);
    }

    @Override
    public Map<UUID, EntityCubit> getEntities() {
        return cubits;
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), "cubit")
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), "cubit");
    }

    public static long cooldown = 0;
    @Override
    public void interact(EntityCubit entity) {
        if (System.currentTimeMillis() < cooldown) return;
        UChat.chat("§b[PET] " + entity.ownerName + "'s Cubit§f: " + getDialog(entity.ownerName));
        cooldown = System.currentTimeMillis() + 1000*2;
    }

    @Override
    public EntityCubit getEntity(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            return cubits.values().stream().filter(e -> e.armorStand.getUniqueID().equals(entity.getUniqueID())).findFirst().orElse(null);
        }
        return super.getEntity(entity);
    }

    public static String getDialog(String name) {
        int num = BUtils.randomInt(0, 6);
        switch (num) {
            case 0:
                return "i have committed several warcrimes!";
            case 1:
                return "What's the difference between goats and fences? I don't know, I'm a robot";
            case 2:
                return "§oEmulating stereotipical robot noises";
            case 3:
                return "§5(§d⌐§c■§6_§e■§b)§3ノ§9♬§f party mode";
            case 4:
                return "I'm not just a robot pet; I'm also a stand-up comedian. Want to hear a robot joke? Why did the robot go on a diet? Because he had too many bytes!";
            case 5:
                return "Why don't robots ever eat junk food? Because they're afraid of becoming bytesized!";
            case 6:
                if (System.currentTimeMillis() > Socket.cachedData.getDouble("lastJoin") + 1000*60*60*60*3) {
                    return "Please touch grass immediately";
                } else {
                    return getDialog(name);
                }
            default:
                return "I have committed several warcrimes!";
        }
    }

    public void spawnPet(EntityPlayer player) {
        if (!canUse(player)) return;
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        EntityCubit hamster = new EntityCubit(theWorld, player.getGameProfile().getName());
        hamster.setPosition(player.posX, player.posY, player.posZ);
        hamster.setOwnerId(player.getUniqueID().toString());
        hamster.setCustomNameTag(player.getName() + "'s Cubit");
        hamster.setAlwaysRenderNameTag(true);
        theWorld.spawnEntityInWorld(hamster);
        cubits.put(player.getUniqueID(), hamster);
    }
}
