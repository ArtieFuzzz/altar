package net

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import com.mongodb.client.result.InsertOneResult
import org.litote.kmongo.*

@kotlinx.serialization.Serializable
data class Paste(val id: String, val content: String)

class DataClient {
     private var client: MongoClient? = null
    private var db: MongoDatabase? = null

    fun connect() {
        this.client = KMongo.createClient()
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