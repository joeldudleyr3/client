package net.corda.joel

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.utilities.NetworkHostAndPort.Companion.parse
import java.util.*
import net.corda.v5.base.concurrent.getOrThrow
import net.corda.sample.datapersistence.flows.CustomSerializerRecordFlow

fun main() {
    val address = parse("localhost:10003")
    val username = "user"
    val password = "test"

    val cordaRpcOpsClient = CordaRPCClient(address)
    val cordaRpcOpsConnection = cordaRpcOpsClient.start(username, password)
    val cordaRpcOpsProxy = cordaRpcOpsConnection.proxy

    cordaRpcOpsProxy.startFlowDynamic(CustomSerializerRecordFlow.Initiator::class.java, "name-${UUID.randomUUID()}")
            .returnValue
            .getOrThrow()

    cordaRpcOpsConnection.close()
}