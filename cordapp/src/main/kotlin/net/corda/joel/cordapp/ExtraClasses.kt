package net.corda.joel.cordapp

import net.corda.core.flows.Flow
import net.corda.core.flows.FlowSession
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.flows.flowservices.dependencies.CordaInject
import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.node.services.persistence.MappedSchema
import net.corda.systemflows.internal.notary.SinglePartyNotaryService
import net.corda.systemflows.internal.notary.UniquenessProvider
import net.corda.v5.ledger.services.LedgerServiceHub
import net.corda.v5.legacyapi.flows.FlowLogic
import net.corda.v5.serialization.CheckpointCustomSerializer
import net.corda.v5.serialization.SerializationCustomSerializer
import net.corda.v5.serialization.SerializationWhitelist
import net.corda.v5.serialization.SingletonSerializeAsToken
import java.security.PublicKey

// Another flow implementation.
@InitiatingFlow
@StartableByRPC
class DummyFlowTwoReturnOfTheFlow : FlowLogic<String>() {
    @CordaInject
    private lateinit var dummyService: DummyService

    override fun call(): String {
        return dummyService.string
    }
}

// Service implementation.
@CordaService
class DummyService(private val serviceHub: AppServiceHub) : SingletonSerializeAsToken() {
    val string = "bing bong"
}

// Serialisation whitelist implementation.
object DummyWhitelist : SerializationWhitelist {
    override val whitelist: List<Class<*>> = listOf()
}

// Mapped schema implementation.
object DummySchema

object DummySchemaV1 : MappedSchema(
        schemaFamily = DummySchema.javaClass,
        version = 1,
        mappedTypes = listOf()) {

    override val migrationResource: String? = null
}

// Custom serializer and checkpoint serializer implementations.
class StringAtom(val atom: String)
class Proxy(val value: String)

class CustomSerializer : SerializationCustomSerializer<StringAtom, Proxy> {
    override fun fromProxy(proxy: Proxy): StringAtom = StringAtom(proxy.value)
    override fun toProxy(obj: StringAtom): Proxy = Proxy(obj.atom)
}

class CheckpointSerializer :
        CheckpointCustomSerializer<StringAtom, Proxy> {

    override fun toProxy(obj: StringAtom): Proxy {
        return Proxy(obj.atom)
    }

    override fun fromProxy(proxy: Proxy): StringAtom {
        return StringAtom("atom")
    }
}

// Custom notary.
class DummyNotaryService : SinglePartyNotaryService() {
    override val uniquenessProvider: UniquenessProvider
        get() = TODO("Not yet implemented")
    override val services: LedgerServiceHub
        get() = TODO("Not yet implemented")
    override val notaryIdentityKey: PublicKey
        get() = TODO("Not yet implemented")

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun createServiceFlow(otherPartySession: FlowSession): Flow<Void?> {
        TODO("Not yet implemented")
    }
}