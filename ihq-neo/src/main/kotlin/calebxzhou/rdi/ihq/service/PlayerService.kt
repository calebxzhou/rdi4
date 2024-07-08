package calebxzhou.rdi.ihq.service

import calebxzhou.rdi.ihq.accountCol
import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.protocol.account.RegisterSPacket
import calebxzhou.rdi.ihq.util.*
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.netty.channel.ChannelHandlerContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import java.util.*

object PlayerService {


    //根据qq获取
    suspend fun getByQQ(qq: String): Account? = accountCol.find(eq("qq", qq)).firstOrNull()
    suspend fun getByName(name: String): Account? = accountCol.find(eq("name", name)).firstOrNull()
    suspend fun getByUuid(uuid: UUID): Account? = accountCol.find(eq("uuid", uuid)).firstOrNull()
    suspend fun getByUuid(uuid: String): Account? = getByUuid(UUID.fromString(uuid))
    suspend fun get(usr: String): Account? {
        var account = getByQQ(usr) ?: getByName(usr)
        if (usr.isValidUuid()) {
            account = getByUuid(UUID.fromString(usr))
        } else if (usr.isValidObjectId()) {
            account = getById(ObjectId(usr))
        }
        return account
    }

    fun equalById(id: ObjectId): Bson {
        return eq("_id", id)
    }

    fun equalById(acc: Account): Bson {
        return eq("_id", acc.id)
    }

    //根据rid获取
    suspend fun getById(id: ObjectId): Account? = accountCol.find(equalById(id)).firstOrNull()


    suspend fun validate(usr: String, pwd: String): Account? {
        val account = get(usr)
        return if (account == null || account.pwd != pwd) {
            null
        } else {
            account
        }
    }

    suspend fun changeQQ(acc: Account, qq: String, ctx: ChannelHandlerContext) {
        if (getByQQ(qq) != null) {
            ctx.err("QQ用过了")
            return
        }
        accountCol.updateOne(equalById(acc), Updates.set("qq", qq))
        ctx.ok()
    }

    suspend fun changeName(acc: Account, name: String, ctx: ChannelHandlerContext) {
        if (getByName(name) != null) {
            ctx.err("名字用过了")
            return
        }
        accountCol.updateOne(equalById(acc), Updates.set("name", name))
        ctx.ok()
    }

    suspend fun changePwd(acc: Account, pwd: String, ctx: ChannelHandlerContext) {
        accountCol.updateOne(equalById(acc), Updates.set("pwd", pwd))
        ctx.ok()
    }

    suspend fun clearCloth(acc: Account, ctx: ChannelHandlerContext) {
        accountCol.updateOne(equalById(acc), Updates.unset("cloth"))
        ctx.ok()
    }
    suspend fun changeCloth(acc: Account, cloth: Account.Cloth, ctx: ChannelHandlerContext) {
        if (!cloth.cape.isValidHttpUrl()) {
            cloth.cape = null
        }
        if (!cloth.skin.isValidHttpUrl()) {
            cloth.skin = null
        }
        if (!cloth.elytra.isValidHttpUrl()) {
            cloth.elytra = null
        }
        accountCol.updateOne(equalById(acc), Updates.set("cloth", cloth))
        ctx.ok()
    }

    suspend fun getSkin(call: ApplicationCall) {
        val uuid = call.parameters["uuid"]
        if (uuid == null) {
            call.respond(HttpStatusCode.BadRequest)
            return
        }
        getByUuid(uuid)?.let {
            it.cloth?.let { cloth ->
                call.respondText(Json.encodeToString(cloth))
            }
        } ?: let {
            call.respond(HttpStatusCode.NotFound)
            return
        }
    }

    suspend fun register(pkt: RegisterSPacket, ctx: ChannelHandlerContext) {
        if (getByQQ(pkt.qq) != null || getByName(pkt.name) != null) {
            ctx.err("QQ或昵称被占用")
            return
        }
        val account = Account(
            name = pkt.name,
            pwd = pkt.pwd,
            qq = pkt.qq
        )
        accountCol.insertOne(account)
        ctx.ok()
    }




}