package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.*

data class RAccount(
    val name: String,
    val uuid: UUID,
    var pwd: String,
)