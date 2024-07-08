package calebxzhou.rdi.ihq.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Team(
    @BsonId val id: ObjectId,
    val name: String,
    val members: List<Member>
){
    constructor(account: Account): this(ObjectId(),account.name+"的团队", listOf(Member(account.id,Role.OWNER)))
    data class Member(
        val id: ObjectId,
        val role: Role
    )
    enum class Role{
        OWNER ,ADMIN, CREW
    }
    fun hasMember(id: ObjectId): Boolean {
       return members.find { it.id==id } != null
    }
    val owner
        get() = members.find { it.role==Role.OWNER }
}