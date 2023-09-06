package cc.woverflow.hysentials.schema

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.neovisionaries.ws.client.WebSocket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

class Hysentials {
    data class User(
        var username: String,
        var uuid: String?,
        var discordId: String?,
        var muted: Muted?,
        var banned: Banned?,
        var firstJoin: Double?,
        var lastJoin: Double?,
        var rank: String?,
        var hasPlus: Boolean?,
        var online: Boolean?,
        var isBoosting: Boolean?,
        var isEarlySupporter: Boolean?,
        var emeralds: Int,
        var exp: Int?,
        var givenLevel: Int? = 0,
        var quests: MutableSet<Quest>?,
        var amountSpent: Int?,
        var hypixel: JsonObject?,
        var hypixel2: JsonObject?,
    )

    @Serializable
    data class Rank(
        var name: String,
        var users: ArrayList<String>,
    )


    @Serializable
    data class Cosmetic(
        var name: String,
        var users: ArrayList<String>,
        var manual: ArrayList<String>,
        var type: String,
        var description: String,
        var equipped: ArrayList<String>,
        var itemID: Int?,
        var rarity: String,
        var cost: Int,
        var color: String?,
        var resource: String?,
        var framerate: Int?,
        var material: String?,
    )

    data class LevelRewards(
        var emeralds: Int? = 0,
        var cosmetic: String? = null,
        var exp: Int? = 0,
    )

    data class Quest(
        var id: String,
        var progress: Int,
        var completed: Boolean,
        var rewards: LevelRewards,
        var activated: Boolean,
        var daily: Boolean,
        var data: JsonObject = JsonObject(),
    )

    @Serializable
    data class Muted(
        var muted: Boolean?,
        var reason: String?,
        var mutedBy: String?,
        var mutedAt: Double?,
        var expire: Double?,
    )

    @Serializable
    data class Banned(
        var banned: Boolean?,
        var reason: String?,
        var bannedBy: String?,
        var bannedAt: Double?,
        var expire: Double?,
    )

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
        var actions: ArrayList<Action>, //TODO Replace with action class later
        var houses: ArrayList<JsonObject>,
        var invites: ArrayList<String>,
        @Transient var isOwner: Boolean? = null,
    )

    data class Action(
        var id: String,
        var action: ActionData,
        var rating: Int,
        var ratingsPositive: Int,
        var ratingsNegative: Int,
        var ratedNegativeBy: ArrayList<String>,
        var ratedPositiveBy: ArrayList<String>,
    )

    data class ActionData(
        var creator: String,
        var code: String,
        var codespace: ActionCodespace,
        var name: String,
        var description: String,
        var type: String?,
        var createTime: Double,
    )

    data class ActionCodespace(
        var functions: Int,
        var conditions: Int,
        var actions: Int,
    )
}