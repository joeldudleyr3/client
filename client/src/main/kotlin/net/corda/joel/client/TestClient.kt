package net.corda.joel.client

import net.corda.client.rpc.CordaRPCClient
import net.corda.joel.cordapp.TwoNodeFlow
import net.corda.v5.base.util.NetworkHostAndPort.Companion.parse

fun main() {
    val address = parse("localhost:10003")
    val username = "user"
    val password = "test"

    val cordaRpcOpsClient = CordaRPCClient(address)
    val cordaRpcOpsConnection = cordaRpcOpsClient.start(username, password)
    val cordaRpcOpsProxy = cordaRpcOpsConnection.proxy

    println(cordaRpcOpsProxy.startFlowDynamic(TwoNodeFlow::class.java).returnValue.get())

    cordaRpcOpsConnection.close()
}