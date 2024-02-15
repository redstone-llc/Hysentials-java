package llc.redstone.hysentials.util;

import cc.polyfrost.oneconfig.platform.Platform;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class BUtils {
    public static int randomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static boolean isInteger(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isHypixelOrSBX() {
        if (!Platform.getServerPlatform().inMultiplayer()) return false;

        String serverBrand = Platform.getServerPlatform().getServerBrand();

        if (serverBrand == null) return false;

        return serverBrand.toLowerCase(Locale.ENGLISH).contains("hypixel") || serverBrand.toLowerCase(Locale.ENGLISH).contains("skyblocksandbox");
    }

    public static boolean isSBX() {
        if (!Platform.getServerPlatform().inMultiplayer()) return false;

        String serverBrand = Platform.getServerPlatform().getServerBrand();

        if (serverBrand == null) return false;

        return serverBrand.toLowerCase(Locale.ENGLISH).contains("skyblocksandbox");
    }

    public static JSONArray nbtListToJSON(NBTTagList list) {
        JSONArray jsonArray = new JSONArray();
        System.out.println(list.toString());
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase nbtBase = list.get(i);
            if (nbtBase instanceof NBTTagByte) {
                jsonArray.put(((NBTTagByte) nbtBase).getByte());
            } else if (nbtBase instanceof NBTTagShort) {
                jsonArray.put(((NBTTagShort) nbtBase).getShort());
            } else if (nbtBase instanceof NBTTagInt) {
                jsonArray.put(((NBTTagInt) nbtBase).getInt());
            } else if (nbtBase instanceof NBTTagLong) {
                jsonArray.put(((NBTTagLong) nbtBase).getLong());
            } else if (nbtBase instanceof NBTTagFloat) {
                jsonArray.put(((NBTTagFloat) nbtBase).getFloat());
            } else if (nbtBase instanceof NBTTagDouble) {
                jsonArray.put(((NBTTagDouble) nbtBase).getDouble());
            } else if (nbtBase instanceof NBTTagByteArray) {
                jsonArray.put(((NBTTagByteArray) nbtBase).getByteArray());
            } else if (nbtBase instanceof NBTTagString) {
                jsonArray.put(((NBTTagString) nbtBase).getString());
            } else if (nbtBase instanceof NBTTagList) {
                jsonArray.put(nbtListToJSON((NBTTagList) nbtBase));
            } else if (nbtBase instanceof NBTTagCompound) {
                jsonArray.put(nbtCompoundToJson((NBTTagCompound) nbtBase));
            }
        }
        return jsonArray;
    }

    public static JSONObject nbtCompoundToJson(NBTTagCompound nbt) {
        JSONObject jsonObject = new JSONObject();
        System.out.println(nbt.toString());
        if (nbt != null) {
            for (String key : nbt.getKeySet()) {
                if (nbt.getTag(key) instanceof NBTTagByte) {
                    jsonObject.put(key, nbt.getByte(key));
                } else if (nbt.getTag(key) instanceof NBTTagShort) {
                    jsonObject.put(key, nbt.getShort(key));
                } else if (nbt.getTag(key) instanceof NBTTagInt) {
                    jsonObject.put(key, nbt.getInteger(key));
                } else if (nbt.getTag(key) instanceof NBTTagLong) {
                    jsonObject.put(key, nbt.getLong(key));
                } else if (nbt.getTag(key) instanceof NBTTagFloat) {
                    jsonObject.put(key, nbt.getFloat(key));
                } else if (nbt.getTag(key) instanceof NBTTagDouble) {
                    jsonObject.put(key, nbt.getDouble(key));
                } else if (nbt.getTag(key) instanceof NBTTagByteArray) {
                    jsonObject.put(key, nbt.getByteArray(key));
                } else if (nbt.getTag(key) instanceof NBTTagString) {
                    jsonObject.put(key, nbt.getString(key));
                } else if (nbt.getTag(key) instanceof NBTTagList) {
                    jsonObject.put(key, nbtListToJSON((NBTTagList) nbt.getTag(key)));
                } else if (nbt.getTag(key) instanceof NBTTagCompound) {
                    jsonObject.put(key, nbtCompoundToJson(nbt.getCompoundTag(key)));
                }
            }
        }
        return jsonObject;
    }
}
