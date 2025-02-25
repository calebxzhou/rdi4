package calebxzhou.rdi.ihq.service

import calebxzhou.rdi.ihq.db
import calebxzhou.rdi.ihq.exception.RequestError
import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.model.Island
import calebxzhou.rdi.ihq.service.IslandService.getJoinedIsland
import calebxzhou.rdi.ihq.util.e400
import calebxzhou.rdi.ihq.util.e401
import calebxzhou.rdi.ihq.util.e500
import calebxzhou.rdi.ihq.util.got
import calebxzhou.rdi.ihq.util.ok
import calebxzhou.rdi.ihq.util.uid
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
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
    //建岛
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
    suspend fun delete(call: ApplicationCall){
        val island = getOwnIsland(call.uid) ?: let {
            call.e400("没岛")
            return
        }
        dbcl.deleteOne(eq("_id", island.id))
        // todo 向mc服务器发送rcon指令 清存档+删岛
        call.ok()
    }
    //回到自己拥有/加入的岛屿
    suspend fun home(call: ApplicationCall) {
        val island = getJoinedIsland(call.uid)?: let {
            call.e400("没岛")
            return
        }
        // todo mc-rcon 传送

    }
    //设传送点
    suspend fun sethome(call: ApplicationCall) {
        val params = call.receiveParameters()
        val homePos = params["pos"]?: Island.DEFAULT_HOMEPOS
        val island = getOwnIsland(call.uid)?: let {
            call.e400("必须岛主来做")
            return
        }
        //todo 客户端检查脚下实心方块
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("homePos",homePos)
        )
        call.ok()
    }
    //退出岛屿
    suspend fun quit(call: ApplicationCall) {
        val island = getJoinedIsland(call.uid) ?: let {
            call.e400("没岛")
            return
        }
        if (island.owner.id == call.uid) {
            call.e400("你是岛主，只能删除")
            return
        }
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.pull("members", eq("id",call.uid))
        )
        //todo 删玩家档
        call.ok()
    }

    //邀请玩家加入岛屿
    suspend fun invite(call: ApplicationCall) {
        val params = call.receiveParameters()
        val uid1 = call.uid
        val uid2 = ObjectId(params got "uid2")

        val island = getOwnIsland(uid1) ?: let {
            throw RequestError("你没岛")
        }
        if (getJoinedIsland(uid2) != null) {
            throw RequestError("他有岛")
        }
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.push("members", Island.Member(uid2, false))
        )
        call.ok()
    }
    //踢出
    suspend fun kick(call: ApplicationCall) {
        val params = call.receiveParameters()
        val uid1 = call.uid
        val uid2 = ObjectId(params got "uid2")
        if(uid1 == uid2){
            throw RequestError("不能踢自己")
        }
        val island = getOwnIsland(uid1) ?: let {
            throw RequestError("你没岛")
            return
        }
        if (!island.hasMember(uid2)) {
            throw RequestError("他不是岛员")
            return
        }
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.pull("members", eq("pid",uid2))
        )
        //todo 删除对方存档
        call.ok()
    }
    //转让
    suspend fun transfer(call: ApplicationCall) {
        val params = call.receiveParameters()
        val uid1 = call.uid
        val uid2 = ObjectId(params got "uid2")
        val island = getOwnIsland(uid1) ?: let {
            throw RequestError("你没岛")
            return
        }
        if (!island.hasMember(uid2)) {
            throw RequestError("他不是岛员")
            return
        }
        //给对方加上岛主权限
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("members.$[element].isOwner", true),
            UpdateOptions().arrayFilters(listOf(eq("element.pid", uid2)))
        )
        //给自己去掉岛主权限
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("members.$[element].isOwner", false),
            UpdateOptions().arrayFilters(listOf(eq("element.pid", uid1)))
        )
        call.ok()
    }


    suspend fun getById(id: ObjectId): Island? = dbcl.find(eq("_id", id)).firstOrNull()
}