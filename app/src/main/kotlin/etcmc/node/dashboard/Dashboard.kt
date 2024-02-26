package etcmc.node.dashboard

import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.runUntilKeyPressed
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.textLine

fun main() {
    val eventBus = EventBus()
    val server = HttpServer(eventBus)
    val watcher = BalanceWatcher(eventBus)

    session {
        section {
            textLine("Press Q to quit")
            textLine()
        }.onFinishing {
            server.stop()
            watcher.stop()
        }.runUntilKeyPressed(Keys.Q) {
            server.start()
            watcher.start()
            eventBus.subscribe<Event> {
                val balanceChanged = it as Event.BalanceChanged
                println("New balance: ${balanceChanged.balance} at ${balanceChanged.timestamp}")
            }
        }
    }
}
