package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

data class RPlayer(
    @BsonId val id: ObjectId,
    val uuid:UUID,
    val name: String,
    val pwd: String,
    val qq: String,
    val regTime: Long = System.currentTimeMillis(),
    val globalChat: Boolean = true,
){
    override fun toString(): String {
        return "$name($id)"
    }
}
