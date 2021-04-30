package net.corda.joel.cordapp

import net.corda.v5.application.flows.FlowSession
import net.corda.v5.application.flows.InitiatedBy
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.flows.StartableByRPC
import net.corda.v5.application.flows.flowservices.dependencies.CordaInject
import net.corda.v5.application.identity.AbstractParty
import net.corda.v5.application.utilities.unwrap
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData
import net.corda.v5.ledger.services.NotaryAwareNetworkMapCache
import net.corda.v5.ledger.transactions.LedgerTransaction
import net.corda.v5.legacyapi.flows.FlowLogic

@InitiatingFlow
@StartableByRPC
class TwoNodeFlow : FlowLogic<Int>() {
    @CordaInject
    lateinit var networkMapCache: NotaryAwareNetworkMapCache

    @Suspendable
    override fun call(): Int {
        val counterparty = networkMapCache.allNodes[0].legalIdentities[0]
        initiateFlow(counterparty).send("Hello")
        return 0
    }
}

@InitiatedBy(TwoNodeFlow::class)
class TwoNodeFlowResponder(private val otherSide: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        println(otherSide.receive<String>().unwrap { it })
    }
}

@BelongsToContract(DummyContract::class)
data class DummyState(override val participants: List<AbstractParty> = listOf()) : ContractState

class DummyContract : Contract {
    companion object {
        const val ID = "net.corda.joel.cordapp.DummyContract"
    }

    override fun verify(tx: LedgerTransaction) = Unit
}

object DummyCommand : TypeOnlyCommandData()