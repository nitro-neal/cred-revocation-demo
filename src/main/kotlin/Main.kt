import web5.credentials.CreateVcOptions
import web5.credentials.DIDKey
import web5.credentials.SignOptions
import web5.credentials.VcJwt
import web5.credentials.VerifiableCredential
import web5.credentials.model.CredentialSubject
import java.net.URI

fun main(args: Array<String>) {
    val (privateJWK, did, document) = DIDKey.generateEd25519()

    val claims: MutableMap<String, Any> = LinkedHashMap()
    val degree: MutableMap<String, Any> = LinkedHashMap()
    degree["name"] = "Bachelor of Science and Arts"
    degree["type"] = "BachelorDegree"
    claims["college"] = "Test University"
    claims["degree"] = degree

    val credentialSubject = CredentialSubject.builder()
        .id(URI.create(did))
        .claims(claims)
        .build()

    val signOptions = SignOptions(
        kid = "#" + did.split(":")[2],
        issuerDid = did,
        subjectDid = did,
        signerPrivateKey = privateJWK
    )

    val vcCreateOptions = CreateVcOptions(
        credentialSubject = credentialSubject,
        issuer = did,
        expirationDate = null,
        credentialStatus = null
    )

    val vcJwt: VcJwt = VerifiableCredential.create(signOptions, vcCreateOptions, null)

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Created VC: $vcJwt")
}