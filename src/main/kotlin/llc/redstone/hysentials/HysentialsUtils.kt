package llc.redstone.hysentials

import llc.redstone.hysentials.updategui.UpdateChecker
import llc.redstone.hysentials.util.NetworkUtils
import kotlinx.coroutines.*
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.common.MinecraftForge

var HYSENTIALS_API = if (isLocalOn()) "http://localhost:8080/api" else "https://backend.redstone.llc/api"
var WEBSOCKET = if (isLocalOn()) "ws://localhost:8080/ws" else "ws://backend.redstone.llc/ws"

var local = false
lateinit var VERSION: String
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

fun init(version: String) {
    VERSION = version
    println(Launch.blackboard["fml.deobfuscatedEnvironment"])
}

fun postInit() {
    MinecraftForge.EVENT_BUS.register(UpdateChecker())
    UpdateChecker.instance.downloadDeleteTask()
}

