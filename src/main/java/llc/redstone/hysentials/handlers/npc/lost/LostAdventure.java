package llc.redstone.hysentials.handlers.npc.lost;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.handlers.misc.QuestHandler;
import llc.redstone.hysentials.handlers.npc.NPC;
import llc.redstone.hysentials.mixin.GuiNewChatAccessor;
import llc.redstone.hysentials.quest.Quest;
import com.mojang.authlib.GameProfile;
import llc.redstone.hysentials.handlers.misc.QuestHandler;
import llc.redstone.hysentials.handlers.npc.NPC;
import llc.redstone.hysentials.mixin.GuiNewChatAccessor;
import llc.redstone.hysentials.quest.Quest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.entity.Entity;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LostAdventure extends NPC {
    public int lastX = 0;
    public int lastY = 0;
    public int lastZ = 0;

    public int startX = 0;
    public int startY = 0;
    public int startZ = 0;

    public LostRidable lostRidable;
    Field field;

    public LostAdventure() {
        super(
            new GameProfile(UUID.randomUUID(), "§e§lCLICK ME"),
            "hysentials:npc/lost_adventure.png",
            true
        );
        try {
            field = ReflectionHelper.findField(ChatLine.class, "field_74541_b", "lineString");
            field.setAccessible(true);
        } catch (ReflectionHelper.UnableToFindFieldException e) {
        }
        hologram.add("§b??????");
    }

    @Override
    public void spawnNPC(int x, int y, int z) {
        super.spawnNPC(x, y, z);
    }

    public int chatID = 0;

    @Override
    public void onWorldLoad() {
        super.onWorldLoad();
        if (entity != null) {
            lastX = startX;
            lastY = startY;
            lastZ = startZ;
            interactions = 0;
            chatID = 0;
            entity.setDead();
            entity = null;
            if (lostRidable != null) {
                lostRidable.setDead();
                lostRidable = null;
            }
        }
    }

    @Override
    public void onMessageRecieve(ClientChatReceivedEvent event) {
        if (event.message.getFormattedText().replace("§r", "").equals("     §e§lProtect your bed and destroy the enemy beds.")) {
            Multithreading.schedule(() -> {
                BlockPos pos = checkPosition(40);
                lastX = pos.getX();
                lastY = pos.getY();
                lastZ = pos.getZ();
                startX = lastX;
                startY = lastY;
                startZ = lastZ;
                spawnNPC(lastX, lastY, lastZ);

                UTextComponent textComponent = new UTextComponent("&e[NPC] ??????&f: *Groans* How did I get here? &e(Hover for info)");
                textComponent.setHover(HoverEvent.Action.SHOW_TEXT, "§7Someone has been here before you...\n§7Maybe you should ask them what happened?\n§7Distance: §e" + distance(startX, startY, startZ) + " §7blocks away");
                chatID = new Random().nextInt(Integer.MAX_VALUE - 1);
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textComponent, chatID);

            }, 1, TimeUnit.SECONDS);
        }
    }

    public int distance(int posx, int posy, int posz) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX);
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY);
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ);

        double x = posx - viewerX + 0.5f;
        double y = posy - viewerY - viewer.getEyeHeight();
        double z = posz - viewerZ + 0.5f;

        double distSq = x * x + y * y + z * z;
        double dist = Math.sqrt(distSq);
        return (int) dist;
    }

    @Override
    public void onWorldRender(RenderWorldLastEvent event) {
        super.onWorldRender(event);
        try {
            if (entity != null && !LocrawUtil.INSTANCE.isInGame()) {
                lastX = startX;
                lastY = startY;
                lastZ = startZ;
                interactions = 0;
                chatID = 0;
                entity.setDead();
                entity = null;
                if (lostRidable != null) {
                    lostRidable.setDead();
                    lostRidable = null;
                }
            }
            if (chatID != 0 && entity != null) {
                UTextComponent textComponent = new UTextComponent("&e[NPC] ??????&f: *Groans* How did I get here? &e(Hover for info)");
                textComponent.setHover(HoverEvent.Action.SHOW_TEXT, "§7Someone has been here before you...\n§7Maybe you should ask them what happened?\n§7Can be found §e" + distance(startX, startY, startZ) + " §7blocks away");

                Iterator<ChatLine> iterator = ((GuiNewChatAccessor) Minecraft.getMinecraft().ingameGUI.getChatGUI()).getDrawnChatLines().iterator();
                ChatLine chatLine = null;
                while (iterator.hasNext()) {
                    chatLine = iterator.next();
                    if (chatLine.getChatLineID() == chatID) {
                        break;
                    }
                }
                if (chatLine == null) {
                    return;
                }
                try {
                    field.set(chatLine, textComponent);
                } catch (IllegalAccessException e) {
                }
            }

            if (entity != null && entity.posY < 0 && interactions == 2) {
                System.out.println("Fell into the void");
                UChat.chat("&e[NPC] Lost Mage&f: NOOOOOOOOO");
                lostRidable.setPositionAndRotation(startX, startY, startZ, 0, 0);
                lastX = startX;
                lastY = startY;
                lastZ = startZ;
                entity.setPositionAndRotation(startX, startY, startZ, 0, 0);
            }

            if (entity != null && (entity.posX < 10 && entity.posX > -10 && entity.posZ < 10 && entity.posZ > -10) && interactions == 2) {
                System.out.println(entity.posX + " " + entity.posZ);
                lastX = startX;
                lastY = startY;
                lastZ = startZ;
                interactions = 0;
                chatID = 0;
                messageDelayed("&e[NPC] Lost Mage&f: Thank you for helping me adventurer!", 0);
                messageDelayed("&e[NPC] Lost Mage&f: Here is your reward.", 2);
                messageDelayed("&e[NPC] Lost Mage&f: I hope to see you again soon.", 4);
                messageDelayed("&e[NPC] Lost Mage&f: *Casts a spell and disappears*", 6);
                Multithreading.schedule(() -> {
                    entity.setDead();
                    entity = null;
                    if (lostRidable != null) {
                        lostRidable.setDead();
                        lostRidable = null;
                    }
                    messageDelayed("\n" +
                        "&a&lQuest Complete\n" +
                        "&7Accompany the Lost mage to the middle of the map &a(0,0) &7where they will return to where they came from.\n" +
                        "&a &b &c &a&lRewards\n" +
                        "   &8+&320,000 Hysentials XP\n" +
                        "   &8+&a400 Emeralds", 1);
                    QuestHandler.checkQuest(Quest.getQuestById("LOST_MAGE"));
                }, 6, TimeUnit.SECONDS);
            }
        } catch (Exception ignored) {
        }
    }

    public int interactions = 0;
    public long cooldown = 0;
    @Override
    public void onInteract(PlayerInteractEvent event, MovingObjectPosition obj) {
        if (System.currentTimeMillis() < cooldown) {
            return;
        }
        switch (interactions) {
            case 0: {
                interactions++;
                cooldown = System.currentTimeMillis() + 1000 * 12;
                messageDelayed("&e[NPC] Lost Mage&f: Why hello traveller, uhmm... This is embarrassing", 0);
                messageDelayed("&e[NPC] Lost Mage&f: One of my spell casts must have went horribly wrong. Thus I have ended up here.", 2);
                messageDelayed("&e[NPC] Lost Mage&f: Can you help me get to the middle of the map? So I can attempt to return to my home.", 6);
                messageDelayed("&e[NPC] Lost Mage&f: I will reward you with something that may have value to you.", 10);
                messageDelayed("&e[NPC] Lost Mage&f: Click me again to start my challenge.", 12);
                hologram.set(0, "§bLost Mage");
                hologramEntities.get(0).setCustomNameTag("§bLost Mage");
                setName("§e§lCLICK TO ESCORT");
                break;
            }
            case 1: {
                interactions++;
                cooldown = System.currentTimeMillis() + 1000 * 10;
                messageDelayed("&e[NPC] Lost Mage&f: Thank you for helping me, I will be sure to tell my friends about you.", 0);
                messageDelayed("&e[NPC] Lost Mage&f: Now please don't let me fall into the void, this world is unstable.", 4);
                messageDelayed("&e[NPC] Lost Mage&f: If I do, I will be teleported back to the location you found me.", 8);
                messageDelayed("\n" +
                    "&a&lQuest Started\n" +
                    "&7Accompany the Lost mage to the middle of the map &a(0,0) &7where they will return to where they came from.\n" +
                    "&a &b &c &a&lRewards\n" +
                    "   &8+&320,000 Hysentials XP\n" +
                    "   &8+&a400 Emeralds", 10);
                Multithreading.schedule(() -> {
                    hologram.set(0, "§bLost Mage");
                    hologramEntities.get(0).setCustomNameTag("§bLost Mage");
                    setName("§e§lFOLLOWING YOU");
                    lostRidable = new LostRidable(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer, this);
                    lostRidable.setPosition(lastX, lastY, lastZ);
                    lostRidable.setOwnerId(Minecraft.getMinecraft().thePlayer.getUniqueID().toString());
                    Minecraft.getMinecraft().theWorld.addEntityToWorld(lostRidable.getEntityId(), lostRidable);
                    lostRidable.setLocationAndAngles(lastX, lastY, lastZ, 0, 0);
                }, 10, TimeUnit.SECONDS);
                break;
            }
        }
    }

    public static void messageDelayed(String message, int delay) {
        Multithreading.schedule(() -> {
            UChat.chat(message);
        }, delay, TimeUnit.SECONDS);
    }
}
