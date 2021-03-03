package net.corda.joel.cordapp

import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.flows.flowservices.dependencies.CordaInject
import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.serialization.CheckpointCustomSerializer
import net.corda.core.serialization.SerializationWhitelist
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.transactions.SignedTransaction
import net.corda.v5.serialization.SerializationCustomSerializer
import javax.persistence.Entity
import javax.persistence.Table

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