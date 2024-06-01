package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class RPlayer(
    @BsonId val id: ObjectId,
    val name: String,
    val pwd: String,
    val qq: String,
    val regTime: Long = System.currentTimeMillis(),
    val globalChat: Boolean = true,
)  {
    companion object{
        val now: RPlayer? = null
    }
}