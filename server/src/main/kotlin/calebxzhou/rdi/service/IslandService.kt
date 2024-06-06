package calebxzhou.rdi.service

import calebxzhou.rdi.db
import calebxzhou.rdi.model.Island
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

object IslandService {
    private val dbcl = db.getCollection<Island>("island")
    private suspend fun getById(id: ObjectId) : Island? = dbcl.find(eq("_id",id)).firstOrNull()

}