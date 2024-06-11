package calebxzhou.rdi.model

import calebxzhou.rdi.Const
import calebxzhou.rdi.log
import calebxzhou.rdi.util.nickname
import net.minecraft.server.level.ServerPlayer
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

data class Island(
    @BsonId val id: ObjectId,
    val name: String,
    val createTime: Long ,
    val homePos: Long,
    val members: List<IslandMember>
){
    fun hasMember(pid: UUID): Boolean {
       return members.find { it.pid==pid } != null
    }
    val owner
        get() = members.find { it.role==IslandRole.OWNER } ?: let{
            log.warn("找不到岛屿$id 的岛主")
            IslandMember(UUID.randomUUID(),IslandRole.OWNER)
        }
    constructor(player: ServerPlayer): this(ObjectId(),player.nickname+"的岛屿",System.currentTimeMillis(),Const.BASE_POS.asLong(),listOf(IslandMember(player.uuid, IslandRole.OWNER)))
}
data class IslandMember(
    val pid:UUID,
    val role: IslandRole,
)
enum class IslandRole {
    OWNER ,ADMIN, CREW
}