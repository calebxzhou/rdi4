package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

data class RAccount(
    @BsonId val id: ObjectId,
    val uuid: UUID,
    val name: String,
    var pwd: String,
    val qq: String,
    val regTime: Long = System.currentTimeMillis(),
    val score:Int = 0
)
