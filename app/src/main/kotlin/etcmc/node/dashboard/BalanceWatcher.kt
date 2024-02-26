package etcmc.node.dashboard

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import kotlin.io.path.readText

class BalanceWatcher(private val eventBus: EventBus) {
    companion object {
        private const val FILE_NAME = "write_only_etcpow_balance.txt"
        private val file = File("./").toPath()
    }

    private val watchService = FileSystems.getDefault().newWatchService()
    private val pathKey = file.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)

    suspend fun start() = withContext(Dispatchers.IO) {
        while (true) {
            ensureActive()
            for (event in pathKey.pollEvents()) {
                val timestamp = Clock.System.now()
                (event.context() as? Path)
                    ?.takeIf { it.fileName.toString() == FILE_NAME }
                    ?.readText()
                    ?.toDoubleOrNull()
                    ?.let { Event.BalanceChanged(balance = it, timestamp = timestamp) }
                    ?.let { eventBus.publish(it) }
            }

            if (!pathKey.reset()) {
                stop()
                break
            }
        }
    }

    fun stop() = runBlocking(Dispatchers.IO) {
        pathKey.cancel()
        watchService.close()
    }
}