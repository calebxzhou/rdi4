package calebxzhou.rdi.service

import calebxzhou.rdi.Const
import calebxzhou.rdi.db
import calebxzhou.rdi.model.Island
import calebxzhou.rdi.model.IslandMember
import calebxzhou.rdi.model.IslandRole
import calebxzhou.rdi.util.*
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SaplingBlock
import org.bson.types.ObjectId
import xyz.nucleoid.fantasy.Fantasy

object IslandService {
    //创建岛屿
    suspend fun create(player: ServerPlayer) {
        if (player.joinedIsland() != null) {
            player.err("有岛")
            return
        }
        player.chat("开始创建岛屿，禁止触碰鼠标键盘")
        val iid = dbcl.insertOne(
            Island(player)
        ).insertedId?.asObjectId()?.value?.toString() ?: let {
            player.err("创建岛屿失败：iid=null")
            return
        }
        mc.execute {
            val fantasy = Fantasy.get(mc)
            val world = fantasy.openIsland(iid).asWorld()
            val basePos = Const.BASE_POS
            world.placeBlock(basePos.offset(0, 0, 0), Blocks.DIRT)
            world.placeBlock(basePos.offset(0, 0, 1), Blocks.DIRT)
            world.placeBlock(basePos.offset(0, 1, 1), Blocks.OAK_SAPLING)
            //player.setSpawn(world,basePos)
            player.teleportTo(world, basePos.offset(0,4,0))
            player.slowfall()
            player.ok("岛屿创建完成，欢迎来到RDI空岛")
        }
    }

    //删除岛屿
    suspend fun delete(player: ServerPlayer) {
        val island = player.ownIsland() ?: let {
            player.err("没岛")
            return
        }
        dbcl.deleteOne(eq("_id", island.id))
        mc.execute {
            player.reset()
            Fantasy.get(mc).openIsland(island.id).delete()
            player.ok("已删除岛屿")
        }
    }

    //回到自己拥有/加入的岛屿
    suspend fun home(player: ServerPlayer) {
        val island = player.joinedIsland()?: let {
            player.err("没岛")
            return
        }
        mc.execute {
            val lvl = Fantasy.get(mc).openIsland(island.id).asWorld()
            player.teleportTo(lvl, island.homePos)
            player.slowfall()
            player.ok()
        }

    }
    suspend fun sethome(player: ServerPlayer) {
        val island = player.ownIsland()?: let {
            player.err("必须岛主来做")
            return
        }
        player.feetBlockState.isSolid
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("homePos",player.blockPosition().asLong())
        )
        player.ok("${player.blockPosition()}")


    }
    //退出岛屿
    suspend fun quit(player: ServerPlayer) {
        val island = player.joinedIsland() ?: let {
            player.err("没岛")
            return
        }
        if (island.owner.pid == player.uuid) {
            player.err("你是岛主，只能删除")
            return
        }
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.pull("members", eq("pid",player.uuid))
        )
        player.reset()
        player.ok("已退岛")
    }

    //邀请玩家加入岛屿
    suspend fun invite(p1: ServerPlayer, p2: ServerPlayer) {
        val island = p1.ownIsland() ?: let {
            p1.err("你没岛")
            return
        }
        if (p2.joinedIsland() != null) {
            p1.err("他有岛")
            return
        }
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.push("members", IslandMember(p2.uuid, IslandRole.CREW))
        )
        p1.ok("已邀请${p2.nickname}加入你的岛屿")
        p2.ok("${p1.nickname}邀请你加入了他的岛屿")
    }
    //踢出
    suspend fun kick(p1: ServerPlayer, p2: ServerPlayer) {
        if(p1 == p2){
            p1.err("不能踢自己")
            return
        }
        val island = p1.ownIsland() ?: let {
            p1.err("你没岛")
            return
        }
        if (!island.hasMember(p2.uuid)) {
            p1.err("他不是岛员")
            return
        }
        dbcl.updateOne(
            eq("_id", island.id),
                    Updates.pull("members", eq("pid",p2.uuid))
        )
        p2.reset()
        p1.ok("已删除岛屿成员 ${p2.name}")
    }
    //转让
    suspend fun transfer(p1: ServerPlayer, p2: ServerPlayer) {
        val island = p1.ownIsland() ?: let {
            p1.err("你没岛")
            return
        }
        if (!island.hasMember(p2.uuid)) {
            p1.err("他不是岛员")
            return
        }
        //给对方加上岛主权限
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("members.$[element].role", IslandRole.OWNER),
            UpdateOptions().arrayFilters(listOf(eq("element.pid", p2.uuid)))
        )
        //给自己去掉岛主权限
        dbcl.updateOne(
            eq("_id", island.id),
            Updates.set("members.$[element].role", IslandRole.CREW),
            UpdateOptions().arrayFilters(listOf(eq("element.pid", p1.uuid)))
        )
        p1.ok("已将岛屿转让给 ${p2.nickname}")
        p2.ok("${p1.nickname}把岛屿转让给你了")
    }

    private val dbcl = db.getCollection<Island>("island")

    //玩家已创建的岛屿
    suspend fun ServerPlayer.ownIsland(): Island? = dbcl.find(
        elemMatch(
            "members", and(
                eq("pid", uuid),
                eq("role", IslandRole.OWNER)
            )
        )
    ).firstOrNull()

    //玩家已加入的岛屿
    suspend fun ServerPlayer.joinedIsland(): Island? = dbcl.find(
        elemMatch(
            "members",and(
                eq("pid", uuid)
            )
        )
    ).firstOrNull()

    suspend fun getById(id: ObjectId): Island? = dbcl.find(eq("_id", id)).firstOrNull()
    fun growTree(p1: ServerPlayer) {
        val bpos = p1.blockPosition()
        val bstate = p1.serverLevel().getBlockState(bpos)
        val block = bstate.block
            if(block is SaplingBlock){
                block.advanceTree(p1.serverLevel(),bpos,bstate,p1.serverLevel().random)
                p1.ok()
            }
        else{
            p1.chat("请站在树苗上")
        }
    }

    fun toggleRain(p1: ServerPlayer) {
        val level = p1.serverLevel()
        if(!level.isRaining)
            {
                level.setWeatherParameters(0, ServerLevel.RAIN_DURATION.sample(level.random), true, true)
                p1.ok("开始下雨")
        } else {
            level.setWeatherParameters( ServerLevel.RAIN_DELAY.sample(level.random), 0, false, false)
            p1.ok("停雨")
        }
    }

    /*fun randomTick(p1: ServerPlayer) {
        val pos = p1.blockPosition()
        p1.serverLevel().getBlockState(pos).randomTick(p1.serverLevel(),pos,p1.serverLevel().random)
        p1.ok()
    }*/
}