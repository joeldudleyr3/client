package net.corda.joel

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.utilities.NetworkHostAndPort.Companion.parse

fun main() {
    val address = parse("localhost:10003")
    val username = "user"
    val password = "test"

    val cordaRpcOpsClient = CordaRPCClient(address)
    val cordaRpcOpsConnection = cordaRpcOpsClient.start(username, password)
    val cordaRpcOpsProxy = cordaRpcOpsConnection.proxy

    println("Do something with $cordaRpcOpsProxy!")
    println(cordaRpcOpsProxy.startFlowDynamic(DummyFlowTwoReturnOfTheFlow::class.java).returnValue.get())

    cordaRpcOpsConnection.close()
}