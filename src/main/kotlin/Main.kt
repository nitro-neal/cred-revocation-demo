import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.jackson.jackson
import java.net.URI
import web5.sdk.credentials.StatusList2021Entry
import web5.sdk.credentials.StatusListCredential
import web5.sdk.credentials.StatusPurpose
import web5.sdk.credentials.VerifiableCredential
import web5.sdk.crypto.InMemoryKeyManager
import web5.sdk.dids.DidKey
data class StreetCredibility(val localRespect: String, val legit: Boolean)

val keyManager = InMemoryKeyManager()
val issuerDid = DidKey.create(keyManager)
val holderDid = DidKey.create(keyManager)

fun main(args: Array<String>) {
    println("Starting Status List Credential Flow Run..")

    val credentialStatus1 = StatusList2021Entry.builder()
        .id(URI.create("cred-with-status-id"))
        .statusPurpose("revocation")
        .statusListIndex("123")
        .statusListCredential(URI.create("http://localhost:1234"))
        .build()

    val credWithCredStatus1 = VerifiableCredential.create(
        type = "StreetCred",
        issuer = issuerDid.uri,
        subject = holderDid.uri,
        data = StreetCredibility(localRespect = "high", legit = true),
        credentialStatus = credentialStatus1
    )

    val statusListCredential1 = StatusListCredential.create(
        "http://localhost:1234",
        issuerDid.uri,
        StatusPurpose.REVOCATION,
        listOf(credWithCredStatus1))

    val signedStatusListCredential = statusListCredential1.sign(issuerDid)

    // Host status list credential
    hostStatusListCred(signedStatusListCredential)

    val revoked = StatusListCredential.validateCredentialInStatusList(credWithCredStatus1)
    require(revoked == true)

    val credentialStatus2 = StatusList2021Entry.builder()
        .id(URI.create("cred-with-status-id"))
        .statusPurpose("revocation")
        .statusListIndex("124")
        .statusListCredential(URI.create("http://localhost:1234"))
        .build()

    val credWithCredStatus2 = VerifiableCredential.create(
        type = "StreetCred",
        issuer = issuerDid.uri,
        subject = holderDid.uri,
        data = StreetCredibility(localRespect = "high", legit = true),
        credentialStatus = credentialStatus2
    )

    val revoked2 = StatusListCredential.validateCredentialInStatusList(credWithCredStatus2)
    require(revoked2 == false)

    println("Status List Credential Flow Ran Successfully!")
}
fun hostStatusListCred(vcJwt: String) {
    embeddedServer(Netty, port = 1234) {
        install(ContentNegotiation) {
            jackson { jacksonObjectMapper() }
        }

        routing {
            get("/") {
                call.respond(HttpStatusCode.OK, vcJwt)
            }
        }
    }.start(wait = false)
}