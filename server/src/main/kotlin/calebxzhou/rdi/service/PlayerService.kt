package calebxzhou.rdi.service

import calebxzhou.rdi.db
import calebxzhou.rdi.model.RPlayer
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.firstOrNull

object PlayerService {
    private val dbcl = db.getCollection<RPlayer>("player")

    //根据qq获取
    suspend fun getByQQ(qq: String): RPlayer? = dbcl.find(eq("qq", qq)).firstOrNull()
}