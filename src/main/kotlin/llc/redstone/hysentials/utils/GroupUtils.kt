package llc.redstone.hysentials.utils

import llc.redstone.hysentials.websocket.Socket

class GroupUtils {
    companion object {
        @JvmStatic
        fun getGroupByName(name: String) = Socket.cachedGroups.find { it.name == name }
        @JvmStatic
        fun getGroupById(id: String) = Socket.cachedGroups.find { it.id == id }
    }
}