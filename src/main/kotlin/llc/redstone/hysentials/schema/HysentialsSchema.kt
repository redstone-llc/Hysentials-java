package llc.redstone.hysentials.schema

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.neovisionaries.ws.client.WebSocket
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.item.ItemStack
import org.json.JSONObject

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
        var hypixel2: JsonObject,
        var skin: String,
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
                    obj["hasPlus"]?.asBoolean?: false,
                    obj["online"]?.asBoolean?: false,
                    obj["isBoosting"]?.asBoolean?: false,
                    obj["isEarlySupporter"]?.asBoolean?: false,
                    obj["emeralds"]?.asInt ?: 0,
                    obj["exp"].asInt,
                    obj["givenLevel"].asInt,
                    obj["quests"]?.asJsonArray?.map { it1 -> Quest.deserialize(it1.asJsonObject) }?.toMutableSet(),
                    obj["amountSpent"]?.asInt,
                    obj["hypixel"].asJsonObject,
                    obj["hypixel2"].asJsonObject,
                    obj["skin"].asString,
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
    ) {
        companion object {
            fun deserialize(obj: JsonObject): Cosmetic {
                return Cosmetic(
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
            }
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
        fun sendWithAuth(method: String, data: JsonObject) {
            data.add("method", JsonPrimitive(method))
            data.add("serverId", JsonPrimitive(serverId))
            data.add("uuid", JsonPrimitive(uuid))
            socket?.sendText(data.toString())
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
                    obj.get("type")?.asString?: "cluster",
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
}