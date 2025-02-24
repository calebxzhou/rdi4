package calebxzhou.rdi.ihq.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Island(
    @Contextual @BsonId val id: ObjectId = ObjectId(),
    val name: String ,
    val homePos: Long =65L,
    val members: List<Member>=arrayListOf(),
    ){
    data class Member(
        val id: ObjectId,
        val isOwner: Boolean
    )
}