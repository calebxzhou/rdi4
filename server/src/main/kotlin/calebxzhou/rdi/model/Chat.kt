package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.*

data class Chat(
    @BsonId val id: ObjectId,
    val uuid: UUID,
    val msg:String,
)
