package net

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.result.InsertOneResult
import kotlinx.serialization.Serializable
import org.litote.kmongo.KMongo
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

@Serializable
data class Paste(val id: String, val content: String)

class DataClient {
    private var client: MongoClient? = null
    private var db: MongoDatabase? = null
    private var uri: String = System.getenv("MONGO_URI") ?: "mongodb://localhost:27017/admin"

    fun connect() {
        this.client = KMongo.createClient(this.uri)
        this.db = this.client!!.getDatabase("altar")
    }

    fun insertPaste(id: String, content: String): InsertOneResult {
        val col = this.db?.getCollection<Paste>()

        return col!!.insertOne(Paste(id, content))
    }

    fun findPaste(id: String): Paste? {
        val col = this.db?.getCollection<Paste>()

        return col!!.findOne(Paste::id eq id)
    }

    fun pasteExists(id: String): Boolean {
        val col = this.db!!.getCollection<Paste>()

        val paste = col.findOne(Paste::id eq id)

        if (paste !== null) return true

        return false
    }
}