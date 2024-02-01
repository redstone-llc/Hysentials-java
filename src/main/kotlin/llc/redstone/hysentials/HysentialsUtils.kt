package llc.redstone.hysentials

import llc.redstone.hysentials.updateGui.UpdateChecker
import llc.redstone.hysentials.util.NetworkUtils
import kotlinx.coroutines.*
import net.minecraftforge.common.MinecraftForge

var HYSENTIALS_API = if (isLocalOn()) "http://localhost:8080/api" else "https://backend.redstone.llc/api"
var WEBSOCKET = if (isLocalOn()) "ws://localhost:8080/ws" else "ws://backend.redstone.llc/ws"

var local = false
fun isLocalOn(): Boolean {
    if (local) return true
    try {
        var s = NetworkUtils.getString("http://localhost:8080/api/online")
        if (s != null) {
            local = true
            return true
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
}
    return false
}

val IO = object : CoroutineScope {
    override val coroutineContext = Dispatchers.IO + SupervisorJob() + CoroutineName("Hysentials IO")
}

fun init() {
    MinecraftForge.EVENT_BUS.register(UpdateChecker())
}

fun postInit() {
    UpdateChecker.instance.downloadDeleteTask()
}

