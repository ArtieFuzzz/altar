package net.plugins

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import net.Paste
import net.database

@kotlinx.serialization.Serializable
data class Body(val content: String)

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hi cutie~")
        }

        get("/paste/{id}") {
            val id = call.parameters["id"]

            if (id === null) return@get call.respond(HttpStatusCode.BadRequest,"You need to provide an ID")

            val exists = database.pasteExists(id)

            if (!exists) return@get call.respond("Paste doesn't exist")

            val content = database.findPaste(id)!!

            call.respond(content)
        }

        post("/paste/{id}") {
            val id = call.parameters["id"]
            val body = call.receive<Body>()

            if (id === null) return@post call.respond(HttpStatusCode.BadRequest,"You need to provide an ID")

            val exists = id.let { it1 -> database.pasteExists(it1) }

            if (exists) return@post call.respond("Paste already exists")

            database.insertPaste(id.toString(), body.content)

            call.respond(HttpStatusCode.Created, "Created paste!")
        }
    }
}
