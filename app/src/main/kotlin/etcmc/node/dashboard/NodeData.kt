package etcmc.node.dashboard

import kotlinx.datetime.Instant


class NodeData(private val id: String) {
    private var balance: Double = 0.0
    private lateinit var lastUpdated: Instant
}
