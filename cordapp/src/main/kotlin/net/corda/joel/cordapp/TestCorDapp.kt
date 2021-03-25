package net.corda.joel.cordapp

import net.corda.core.flows.Flow
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.flows.flowservices.FlowEngine
import net.corda.core.flows.flowservices.FlowIdentity
import net.corda.core.flows.flowservices.dependencies.CordaInject
import net.corda.core.identity.AbstractParty
import net.corda.core.node.services.NetworkMapCache
import net.corda.systemflows.FinalityFlow
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData
import net.corda.v5.ledger.services.TransactionService
import net.corda.v5.ledger.transactions.LedgerTransaction
import net.corda.v5.ledger.transactions.SignedTransaction
import net.corda.v5.ledger.transactions.TransactionBuilderFactory

@BelongsToContract(DummyContract::class)
data class DummyState(override val participants: List<AbstractParty> = listOf()) : ContractState

class DummyContract : Contract {
    companion object {
        const val ID = "net.corda.joel.cordapp.DummyContract"
    }
    override fun verify(tx: LedgerTransaction) = Unit
}

object DummyCommand : TypeOnlyCommandData()

@InitiatingFlow
@StartableByRPC
class DummyFlow : Flow<SignedTransaction> {
    @CordaInject
    lateinit var flowIdentity: FlowIdentity
    @CordaInject
    lateinit var flowEngine: FlowEngine
    @CordaInject
    lateinit var transactionService: TransactionService
    @CordaInject
    lateinit var transactionBuilderFactory: TransactionBuilderFactory
    @CordaInject
    lateinit var networkMapCache: NetworkMapCache

    @Suspendable
    override fun call(): SignedTransaction {
        val notary = networkMapCache.notaryIdentities[0]

        val txb = transactionBuilderFactory.create().setNotary(notary)
                .addCommand(DummyCommand, flowIdentity.ourIdentity.owningKey)
                .addOutputState(DummyState(listOf(flowIdentity.ourIdentity)), DummyContract.ID)


        val stx = transactionService.signInitial(txb)

        return flowEngine.subFlow(FinalityFlow(stx, listOf()))
    }
}