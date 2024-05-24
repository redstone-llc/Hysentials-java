package llc.redstone.hysentials.websocket.methods

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import llc.redstone.hysentials.schema.HysentialsSchema
import llc.redstone.hysentials.websocket.Channel
import llc.redstone.hysentials.websocket.Socket

class UpdateCache: Channel("data") {
    val jsonParser: JsonParser = JsonParser()
    override fun onReceive(obj: JsonObject) {
        val authUserData = obj["user"].asJsonObject
        val userData = obj["data"].asJsonObject
        val serverData = obj["server"].asJsonObject
        val onlineUsers = obj["users"].asJsonArray
        val groups = obj["groups"].asJsonArray

        Socket.user = HysentialsSchema.AuthUser.deserialize(authUserData)
        Socket.user.socket = Socket.CLIENT
        Socket.cachedUser = HysentialsSchema.User.deserialize(userData)
        Socket.user.cache = Socket.cachedUser
        Socket.cachedServerData = HysentialsSchema.ServerData.deserialize(serverData)
        Socket.cachedUsers.clear()
        Socket.cachedGroups.clear()
        onlineUsers.forEach {
            val user = HysentialsSchema.User.deserialize(jsonParser.parse(it.toString()).asJsonObject)
            Socket.cachedUsers[user.uuid] = user
        }
        groups.forEach {
            val group = HysentialsSchema.Group.deserialize(jsonParser.parse(it.toString()).asJsonObject)
            Socket.cachedGroups.add(group)
        }
    }
}