package calebxzhou.rdi.ihq.service

import calebxzhou.rdi.ihq.db
import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.model.Island
import calebxzhou.rdi.ihq.util.e400
import calebxzhou.rdi.ihq.util.e401
import calebxzhou.rdi.ihq.util.e500
import calebxzhou.rdi.ihq.util.ok
import calebxzhou.rdi.ihq.util.uid
import com.mongodb.client.model.Filters.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveParameters
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

object IslandService {
    private val dbcl = db.getCollection<Island>("island")

    //玩家已创建的岛屿
    suspend fun getOwnIsland(uid: ObjectId): Island? = dbcl.find(
        elemMatch(
            "members", and(
                eq("id", uid),
                eq("isOwner", true)
            )
        )
    ).firstOrNull()

    //玩家所在的岛屿
    suspend fun getJoinedIsland(uid: ObjectId): Island? = dbcl.find(
        elemMatch(
            "members",and(
                eq("id", uid)
            )
        )
    ).firstOrNull()
    suspend fun create(call: ApplicationCall){
        if(getJoinedIsland(call.uid)!=null){
            call.e400("已有岛，必须退出/删除方可创建新岛")
            return
        }
        val player = PlayerService.getById(call.uid)
        if(player==null){
            call.e400("玩家未注册")
            return
        }
        val islandName = player.name+"的岛屿"
        val iid = dbcl.insertOne(Island(name=islandName)).insertedId?.asObjectId()?.value?.toString() ?: let {
            call.e500("创建岛屿失败：iid=null")
            return
        }
        // todo 向mc服务器发送rcon指令 建岛
        call.ok(iid)
    }


    suspend fun getById(id: ObjectId): Island? = dbcl.find(eq("_id", id)).firstOrNull()
}