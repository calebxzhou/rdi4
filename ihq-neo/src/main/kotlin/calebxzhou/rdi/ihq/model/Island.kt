package calebxzhou.rdi.ihq.model

import calebxzhou.rdi.ihq.log
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Island(
    @Contextual @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val homePos: Long = DEFAULT_HOMEPOS,
    val members: List<Member> = arrayListOf()
) {
    fun hasMember(pid: ObjectId): Boolean {
        return members.find { it.id==pid } != null
    }
    val owner
        get() = members.find { it.isOwner } ?: let{
            log.error { "找不到岛屿$id 的岛主" }
            Member(ObjectId(),false)
        }
    companion object{
        const val DEFAULT_HOMEPOS: Long = 65L
    }
    @Serializable
    data class Member(
        @Contextual
        val id: ObjectId,
        val isOwner: Boolean
    )
}
