package etcmc.node.dashboard

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HttpServer(private val eventBus: EventBus, port: Int = 8080) {
    private var balance = 0.0

    init {
        CoroutineScope(Job()).launch {
            eventBus.subscribe<Event.BalanceChanged> {
                balance = it.balance
            }
        }
    }

    private val server = embeddedServer(CIO, port) {
        routing {
            get("/") {
                call.respondText("Balance: $balance")
            }
        }
    }

    fun start() = server.start()

    fun stop() = server.stop()
}