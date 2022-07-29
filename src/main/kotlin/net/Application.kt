package net

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mu.KLogger
import mu.KotlinLogging
import net.plugins.*
import java.lang.management.ManagementFactory

val logger: KLogger = KotlinLogging.logger {}
val database: DataClient = DataClient()

fun main() {
    val os = ManagementFactory.getOperatingSystemMXBean()

    println("Starting Altar")
    println("Host Specs:")
    println("OS: ${os.name} (${os.arch})")
    println("Available Cores: ${os.availableProcessors}")
    println("JRE Version: ${Runtime.version()}")
    println("Kotlin Version: ${KotlinVersion.CURRENT}")
    print("\n")

    // Init database connection
    database.connect()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureRouting()
    }.start(wait = true)
}
