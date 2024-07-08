package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Team(
    @BsonId val id: ObjectId,
    val name: String,
    val ownerId: ObjectId,
    val members: List<ObjectId> = mutableListOf()
)
