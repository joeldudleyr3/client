package net.corda.joel.cordapp

import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.AbstractParty
import net.corda.core.utilities.ProgressTracker
import net.corda.impl.ledger.transactions.TransactionBuilderImpl
import net.corda.systemflows.FinalityFlow
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.contracts.BelongsToContract
import net.corda.v5.ledger.contracts.Contract
import net.corda.v5.ledger.contracts.ContractState
import net.corda.v5.ledger.contracts.TypeOnlyCommandData
import net.corda.v5.ledger.transactions.LedgerTransaction
import net.corda.v5.ledger.transactions.SignedTransaction
import net.corda.v5.legacyapi.flows.FlowLogic

@BelongsToContract(DummyContract::class)
data class DummyState(override val participants: List<AbstractParty> = listOf()) : ContractState

class DummyContract : Contract {
    companion object {
        const val ID = "net.corda.joel.DummyContract"
    }
    override fun verify(tx: LedgerTransaction) = Unit
}

object DummyCommand : TypeOnlyCommandData()

@InitiatingFlow
@StartableByRPC
class DummyFlow : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call(): SignedTransaction {
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        val txb = TransactionBuilderImpl(notary)
                .addCommand(DummyCommand, ourIdentity.owningKey)
                .addOutputState(DummyState(listOf(ourIdentity)), DummyContract.ID)
        val stx = serviceHub.signInitialTransaction(txb)

        return subFlow(FinalityFlow(stx, listOf()))
    }
}