package llc.redstone.hysentials.schema

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.neovisionaries.ws.client.WebSocket
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import llc.redstone.hysentials.cosmetic.CosmeticManager.colorFromRarity
import llc.redstone.hysentials.cosmetic.CosmeticManager.equippedCosmetic
import llc.redstone.hysentials.cosmetic.CosmeticManager.hasCosmetic
import llc.redstone.hysentials.cosmetic.CosmeticManager.previewing
import llc.redstone.hysentials.cosmetics.AbstractCosmetic
import llc.redstone.hysentials.cosmetics.Cosmetic
import llc.redstone.hysentials.guis.container.GuiItem
import llc.redstone.hysentials.util.Material
import llc.redstone.hysentials.utils.formatCapitalize
import llc.redstone.hysentials.websocket.Socket
import net.minecraft.client.model.ModelBase
import net.minecraft.item.ItemStack
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class HysentialsSchema {
    data class User(
        var username: String,
        var uuid: String,
        var discordId: String?,
        var muted: Muted?,
        var banned: Banned?,
        var firstJoin: Double,
        var lastJoin: Double,
        var rank: String,
        var hasPlus: Boolean = false,
        var online: Boolean = false,
        var isBoosting: Boolean = false,
        var isEarlySupporter: Boolean = false,
        var emeralds: Int,
        var exp: Int,
        var givenLevel: Int = 0,
        var quests: MutableSet<Quest>?,
        var amountSpent: Int?,
        var hypixel: JsonObject,
        var skin: String?,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): User {
                return User(
                    obj["username"].asString,
                    obj["uuid"].asString,
                    obj["discordId"]?.asString,
                    obj["muted"]?.let { Muted.deserialize(it.asJsonObject) },
                    obj["banned"]?.let { Banned.deserialize(it.asJsonObject) },
                    obj["firstJoin"].asDouble,
                    obj["lastJoin"].asDouble,
                    obj["rank"].asString,
                    obj["hasPlus"]?.asBoolean ?: false,
                    obj["online"]?.asBoolean ?: false,
                    obj["isBoosting"]?.asBoolean ?: false,
                    obj["isEarlySupporter"]?.asBoolean ?: false,
                    obj["emeralds"]?.asInt ?: 0,
                    obj["exp"].asInt,
                    obj["givenLevel"].asInt,
                    obj["quests"]?.asJsonArray?.map { it1 -> Quest.deserialize(it1.asJsonObject) }?.toMutableSet(),
                    obj["amountSpent"]?.asInt,
                    obj["hypixel"].asJsonObject,
                    obj["skin"].asString,
                )
            }
        }
    }

    data class ServerData(
        var rpc: Boolean?
    ) {
        companion object {
            fun deserialize(obj: JsonObject): ServerData {
                return ServerData(
                    obj["rpc"]?.asBoolean,
                )
            }
        }
    }

    @Serializable
    data class Rank(
        var name: String,
        var users: ArrayList<String>,
    )


    data class Cosmetic(
        var name: String,
        var displayName: String?,
        var users: ArrayList<String>,
        var manual: ArrayList<String>,
        var type: String,
        var subType: String?,
        var description: String,
        var equipped: ArrayList<String>,
        var itemID: Int?,
        var rarity: String,
        var cost: Int,
        var color: String?,
        var resource: String?,
        var framerate: Int?,
        var material: String?,
        var skullOwner: String? = null,
        @Transient var item: ItemStack? = null,
        @Transient var cosmeticModel: llc.redstone.hysentials.cosmetics.Cosmetic? = null,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Cosmetic {
                val cos = Cosmetic(
                    obj["name"].asString,
                    obj["displayName"].asString,
                    obj["users"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["manual"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["type"].asString,
                    obj["subType"]?.asString,
                    obj["description"].asString,
                    obj["equipped"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["itemID"]?.asInt,
                    obj["rarity"].asString,
                    obj["cost"].asInt,
                    obj["color"]?.asString,
                    obj["resource"]?.asString,
                    obj["framerate"]?.asInt,
                    obj["material"]?.asString,
                    obj["skullOwner"]?.asString,
                )
                for (cosmetic in AbstractCosmetic.cosmetics) {
                    if (cosmetic.name == cos.name) {

                        cos.cosmeticModel = cosmetic
                    }
                }
                return cos
            }
        }

        fun toItem(uuid: UUID): ItemStack {
            var type = type
            var subType = subType ?: type
            val name = name.lowercase()
            val itemID = itemID ?: -1
            val description = description
            val rarity = rarity
            val cost = cost
            val emerald = Socket.cachedUser?.emeralds ?: 0
            lateinit var item: ItemStack
            val displayName =
                "&f:${rarity.lowercase()}: <${colorFromRarity(rarity)}>" + (displayName ?: name.formatCapitalize())
            val lore: MutableList<String> = mutableListOf()
            description.split("\n").forEach(lore::add)
            if (equippedCosmetic(uuid, name)) {
                lore.add("")
                lore.add("&aEquipped")
            } else if (hasCosmetic(uuid, name)) {
                lore.add("")
                lore.add("&eClick to equip!")
            } else {
                val costString = if (cost == -1) "FREE" else "$cost⏣"
                if (cost != -1) {
                    lore.add("")
                    lore.add("&7Cost: &a$costString")
                    lore.add("")
                }

                if (cost > 0) {
                    lore.add(if (emerald >= cost) "&aClick to purchase!" else "&cNot enough emeralds!")
                } else if (cost == 0) {
                    lore.add("&eClick to purchase!")
                } else if (cost == -1) {
                    lore.add("")
                    lore.add("&cNot purchasable!")
                }
                if (previewing.contains(name)) {
                    lore.add("&cRight-Click to remove preview!")
                } else {
                    lore.add("&bRight-Click to preview!")
                }
            }
            when (subType) {
                "pet" -> {
                    item = GuiItem.makeMonsterEgg(displayName, 1, itemID, lore)
                }

                "bundle" -> {
                    if (Material.valueOf(material!!) == Material.SKULL_ITEM) {
                        item = GuiItem.makeColorfulSkullItem(displayName, skullOwner!!, 1, lore)
                    } else {
                        item = GuiItem.makeColorfulItem(Material.valueOf(material!!), displayName, 1, 0, lore)
                    }
                }// Make file. Please.

                else -> {
                    item = GuiItem.makeColorfulItem(Material.valueOf(material!!), displayName, 1, 0, lore)
                }
            }
            if (material?.startsWith("LEATHER") == true) {
                GuiItem.setColor(item, color)
            }

//            this.item = item
            return item
        }
    }

    data class LevelRewards(
        var emeralds: Int? = 0,
        var cosmetic: String? = null,
        var exp: Int? = 0,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): LevelRewards {
                return LevelRewards(
                    obj["emeralds"]?.asInt,
                    obj["cosmetic"]?.asString,
                    obj["exp"]?.asInt,
                )
            }
        }
    }

    data class Quest(
        var id: String,
        var progress: Int,
        var completed: Boolean,
        var rewards: LevelRewards?,
        var activated: Boolean,
        var daily: Boolean,
        var data: JsonObject = JsonObject(),
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Quest {
                return Quest(
                    obj["id"].asString,
                    obj["progress"].asInt,
                    obj["completed"].asBoolean,
                    obj["rewards"]?.let {
                        LevelRewards.deserialize(it.asJsonObject)
                    },
                    obj["activated"].asBoolean,
                    obj["daily"].asBoolean,
                    obj["data"].asJsonObject,
                )
            }
        }
    }

    @Serializable
    data class Muted(
        var muted: Boolean?,
        var reason: String?,
        var mutedBy: String?,
        var mutedAt: Double?,
        var expire: Double?,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Muted {
                return Muted(
                    obj["muted"]?.asBoolean,
                    obj["reason"]?.asString,
                    obj["mutedBy"]?.asString,
                    obj["mutedAt"]?.asDouble,
                    obj["expire"]?.asDouble,
                )
            }
        }
    }

    @Serializable
    data class Banned(
        var banned: Boolean?,
        var reason: String?,
        var bannedBy: String?,
        var bannedAt: Double?,
        var expire: Double?,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Banned {
                return Banned(
                    obj["banned"]?.asBoolean,
                    obj["reason"]?.asString,
                    obj["bannedBy"]?.asString,
                    obj["bannedAt"]?.asDouble,
                    obj["expire"]?.asDouble,
                )
            }
        }
    }

    @Serializable
    data class AuthUser(
        var username: String,
        var authenticated: Boolean,
        var uuid: String,
        var id: String,
        var serverId: String,
        @Transient var socket: WebSocket? = null,
        @Transient var cache: User? = null,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): AuthUser {
                return AuthUser(
                    obj["username"].asString,
                    obj["authenticated"].asBoolean,
                    obj["uuid"].asString,
                    obj["id"].asString,
                    obj["serverId"].asString,
                )
            }
        }

        fun sendWithAuth(method: String, data: JSONObject) {
            data.put("method", method)
            data.put("serverId", serverId)
            data.put("uuid", uuid)
            socket?.sendText(data.toString())
        }

        fun sendWithAuth(method: String, key: String, value: Any) {
            val data = JSONObject()
            data.put(key, value)
            sendWithAuth(method, data)
        }
    }

    data class Club(
        var id: String,
        var owner: String,
        var name: String,
        var changeTime: Double,
        var members: ArrayList<String>,
        var alias: String,
        var houses: ArrayList<House>,
        var invites: ArrayList<String>,
        var regex: Boolean = false,
        var replaceText: HashMap<String, String>,
        var icons: HashMap<String, String>,
        @Transient var isOwner: Boolean = false,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Club {
                return Club(
                    obj["id"].asString,
                    obj["owner"].asString,
                    obj["name"].asString,
                    obj["changeTime"].asDouble,
                    obj["members"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["alias"].asString,
                    obj["houses"].asJsonArray.map { House.deserialize(it.asJsonObject) }.toCollection(ArrayList()),
                    obj["invites"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["regex"]?.asBoolean ?: false,
                    obj["replaceText"].asJsonObject.let {
                        val map = HashMap<String, String>()
                        it.entrySet().forEach { entry ->
                            map[entry.key] = entry.value.asString
                        }
                        map
                    },
                    obj["icons"].asJsonObject.let {
                        val map = HashMap<String, String>()
                        it.entrySet().forEach { entry ->
                            map[entry.key] = entry.value.asString
                        }
                        map
                    },
                    obj["isOwner"]?.asBoolean ?: false,
                )
            }
        }
    }

    data class House(
        var nbt: JsonObject,
        var name: String,
        var username: String
    ) {
        fun serialize(): JSONObject {
            val obj = JSONObject()
            obj.put("nbt", JSONObject(nbt.toString()))
            obj.put("name", name)
            obj.put("username", username)
            return obj
        }

        companion object {
            fun deserialize(obj: JsonObject): House {
                return House(
                    obj.getAsJsonObject("nbt"),
                    obj["name"].asString,
                    obj["username"].asString
                )
            }
        }
    }

    data class Action(
        var id: String,
        var action: ActionData,
        var rating: Int,
        var ratingsPositive: Int,
        var ratingsNegative: Int,
        var ratedNegativeBy: ArrayList<String>,
        var ratedPositiveBy: ArrayList<String>,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Action {
                return Action(
                    obj["id"].asString,
                    ActionData.deserialize(obj["action"].asJsonObject),
                    obj["rating"].asInt,
                    obj["ratingsPositive"].asInt,
                    obj["ratingsNegative"].asInt,
                    obj["ratedNegativeBy"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["ratedPositiveBy"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                )
            }
        }
    }

    data class ActionData(
        var creator: String,
        var code: String,
        var codespace: ActionCodespace,
        var name: String,
        var description: String,
        var type: String?,
        var createTime: Double,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): ActionData {
                val codespace = obj.get("codespace").asJsonObject
                return ActionData(
                    obj.get("creator").asString,
                    obj.get("code").asString,
                    ActionCodespace(
                        codespace.get("functions").asInt,
                        codespace.get("conditions").asInt,
                        codespace.get("actions").asInt,
                    ),
                    obj.get("name").asString,
                    obj.get("description").asString,
                    obj.get("type")?.asString ?: "cluster",
                    obj.get("createTime").asDouble,
                )
            }
        }
    }

    data class ActionCodespace(
        var functions: Int,
        var conditions: Int,
        var actions: Int,
    ) {
        companion object {
            fun deserialize(obj: JsonObject): ActionCodespace {
                return ActionCodespace(
                    obj["functions"].asInt,
                    obj["conditions"].asInt,
                    obj["actions"].asInt,
                )
            }
        }
    }

    data class Group(
        var id: String,
        var name: String,
        var members: ArrayList<String>,
        var owner: String,
        var invites: ArrayList<String>,
        var messages: ArrayList<String>,
        var saveLast: Int, //count of messages to save
        var allInvite: Boolean = false,
        //TODO: probably add more things
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Group {
                return Group(
                    obj["id"].asString,
                    obj["name"].asString,
                    obj["members"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["owner"].asString,
                    obj["invites"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["messages"].asJsonArray.map { it.asString }.toCollection(ArrayList()),
                    obj["saveLast"].asInt,
                    obj["allInvite"].asBoolean,
                )
            }
        }
    }
}