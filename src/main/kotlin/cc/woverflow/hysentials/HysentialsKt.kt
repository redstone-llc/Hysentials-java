package cc.woverflow.hysentials

import cc.woverflow.hysentials.gui.UpdateChecker
import cc.woverflow.hysentials.gui.UpdateChecker.downloadDeleteTask
import cc.woverflow.hysentials.utils.CustomColor
import cc.woverflow.hysentials.utils.RegexAsString
import cc.woverflow.hysentials.utils.UUIDAsString
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.minecraftforge.common.MinecraftForge
import java.util.*

class HysentialsKt {
    companion object {
        val IO = object : CoroutineScope {
            override val coroutineContext = Dispatchers.IO + SupervisorJob() + CoroutineName("Hysentials IO")
        }

        val json = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                include(serializersModule)
                contextual(CustomColor::class, CustomColor.Serializer)
                contextual(Regex::class, RegexAsString)
                contextual(UUID::class, UUIDAsString)
            }
        }

        val client = HttpClient(CIO) {
            install(ContentEncoding) {
                deflate(1.0F)
                gzip(0.9F)
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(HttpCache)
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }
            install(UserAgent) {
                agent = "Hysentials/${Hysentials.VERSION}"
            }

            engine {
                endpoint {
                    connectTimeout = 10000
                    keepAliveTime = 5000
                    requestTimeout = 10000
                    socketTimeout = 10000
                }
            }
        }

        fun init() {
            MinecraftForge.EVENT_BUS.register(UpdateChecker)
        }

        fun postInit() {
            downloadDeleteTask()
        }
    }
}