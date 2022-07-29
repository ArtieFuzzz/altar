package net.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cachingheaders.*
import kotlinx.serialization.json.*
import net.logger

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
        header("X-Powered-By", "ArtieFuzzz | <3")
    }
    install(StatusPages) {
        exception<Exception> { call, cause ->
            logger.error("FATAL: ${cause.message}")
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }

        status(HttpStatusCode.NotFound) { call, status ->
            logger.debug("Invalid path ${call.request.path()}")
            call.respondText(text = "404: The route ${call.request.path()} is not valid", status = status)
        }
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    install(CachingHeaders) {
        options { _, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Application.Json -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 3600))
                else -> null
            }
        }
    }
}
