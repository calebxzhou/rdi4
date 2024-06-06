package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Island(
    @BsonId val id: ObjectId,
    val name: String,
    val ownerId: ObjectId,
    val createTime: Long = System.currentTimeMillis(),
    val crew: List<ObjectId> = mutableListOf()
)
