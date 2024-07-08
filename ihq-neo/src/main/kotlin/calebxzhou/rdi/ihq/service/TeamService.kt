package calebxzhou.rdi.ihq.service

import calebxzhou.rdi.ihq.db
import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.model.Team
import calebxzhou.rdi.ihq.util.err
import calebxzhou.rdi.ihq.util.ok
import calebxzhou.rdi.ihq.util.serdesJson
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import io.netty.channel.ChannelHandlerContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString

object TeamService {
    private val dbcl = db.getCollection<Team>("team")

    //玩家拥有的团队
    suspend fun Account.ownTeam() : Team? = dbcl.find(
        elemMatch(
            "members", and(
                eq("id", id),
                eq("role", Team.Role.OWNER)
            )
        )
    ).firstOrNull()

    //玩家已加入的团队
    suspend fun Account.joinedTeam(): Team? = dbcl.find(
        elemMatch(
            "members",and(
                eq("id", id)
            )
        )
    ).firstOrNull()
    suspend fun mine(player: Account,ctx: ChannelHandlerContext){
        val team = player.joinedTeam()?:let {
            ctx.err("没队")
            return
        }
        ctx.ok(serdesJson.encodeToString(team))
    }
    suspend fun create(player: Account,ctx: ChannelHandlerContext){
        if (player.joinedTeam() != null) {
            ctx.err("有队")
            return
        }
        dbcl.insertOne(
            Team(player)
        )
        ctx.ok()
    }
    suspend fun delete(player: Account,ctx: ChannelHandlerContext) {
        val island = player.ownTeam() ?: let {
            ctx.err("没队")
            return
        }
        dbcl.deleteOne(eq("_id", island.id))
        ctx.ok()
    }
    //退出队屿
    suspend fun quit(player: Account,ctx: ChannelHandlerContext) {
        val team = player.joinedTeam() ?: let {
            ctx.err("没队")
            return
        }
        if (team.owner?.id == player.id) {
            ctx.err("你是队长 只能删除")
            return
        }
        dbcl.updateOne(
            eq("_id", team.id),
            Updates.pull("members", eq("id",player.id))
        )
        ctx.ok()
    }

    //邀请玩家加入队屿
    suspend fun invite(p1: Account, p2qq: String,ctx: ChannelHandlerContext) {
        val team = p1.ownTeam() ?: let {
            ctx.err("没队")
            return
        }
        val p2 = PlayerService.getByQQ(p2qq)?:let { 
            ctx.err("查无此人")
            return
        }
        if (p2.joinedTeam() != null) {
            ctx.err("他有队")
            return
        }
        dbcl.updateOne(
            eq("_id", team.id),
            Updates.push("members", Team.Member(p2.id, Team.Role.CREW))
        )
        ctx.ok() 
    }
    //踢出
    suspend fun kick(p1: Account, p2qq: String,ctx: ChannelHandlerContext) {
        if(p1.qq == p2qq){
            ctx.err("不能踢自己")
            return
        }
        val p2 = PlayerService.getByQQ(p2qq)?:let {
            ctx.err("查无此人")
            return
        }
        val island = p1.ownTeam() ?: let {
            ctx.err("你没队")
            return
        }
        if (!island.hasMember(p2.id)) {
            ctx.err("他不是队员")
            return
        }
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.pull("members", eq("id",p2.id))
        )
        ctx.ok()
    }
    //转让
    suspend fun transfer(p1: Account, p2qq: String,ctx: ChannelHandlerContext) {
        val island = p1.ownTeam() ?: let {
            ctx.err("你没队")
            return
        }
        val p2 = PlayerService.getByQQ(p2qq)?:let {
            ctx.err("查无此人")
            return
        }
        if (!island.hasMember(p2.id)) {
            ctx.err("他不是队员")
            return
        }
        //给对方加上队主权限
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("members.$[element].role", Team.Role.OWNER),
            UpdateOptions().arrayFilters(listOf(eq("element.id", p2.id)))
        )
        //给自己去掉队主权限
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("members.$[element].role", Team.Role.CREW),
            UpdateOptions().arrayFilters(listOf(eq("element.id", p1.id)))
        )
        ctx.ok()
    }
}