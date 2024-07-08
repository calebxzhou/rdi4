package calebxzhou.rdi.ihq.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Account(
    @Contextual  @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val pwd: String,
    val qq: String,
    val score:Int = 0,
    val cloth: Cloth ?= null,
){
    @Serializable
    data class Cloth(
        val isSlim :Boolean = false,
        var skin: String?,
        var cape: String?,
        var elytra: String?,
    )

}