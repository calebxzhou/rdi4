package calebxzhou.rdi.ihq.service

import calebxzhou.rdi.ihq.accountCol
import calebxzhou.rdi.ihq.log
import calebxzhou.rdi.ihq.model.Account
import calebxzhou.rdi.ihq.util.*
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
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


    suspend fun clearCloth(call: ApplicationCall) {
        accountCol.updateOne(equalById(call.uid), Updates.unset("cloth"))
        call.ok()
    }

    suspend fun changeCloth(call: ApplicationCall) {
        val params = call.receiveParameters()
        val cloth = Account.Cloth(params["isSlim"].toBoolean(), params["skin"], params["cape"], params["elytra"])
        if (!cloth.cape.isValidHttpUrl()) {
            cloth.cape = null
        }
        if (!cloth.skin.isValidHttpUrl()) {
            cloth.skin = null
        }
        if (!cloth.elytra.isValidHttpUrl()) {
            cloth.elytra = null
        }
        accountCol.updateOne(equalById(call.uid), Updates.set("cloth", cloth))
        call.ok()
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

    suspend fun register(call: ApplicationCall) {
        val params = call.receiveParameters()
        val name = params got "name"
        val pwd = params got "pwd"
        val qq = params got "qq"
        if (getByQQ(qq) != null || getByName(name) != null) {
            call.e400("QQ或昵称被占用")
            return
        }
        val account = Account(
            name = name,
            pwd = pwd,
            qq = qq
        )
        accountCol.insertOne(account)
        call.ok()
    }

    suspend fun login(call: ApplicationCall) {
        val params = call.receiveParameters()
        val usr = params got "usr"
        val pwd = params got "pwd"
        validate(usr, pwd)?.let { account ->
            log.info { "${usr}登录成功" }

            call.ok(serdesJson.encodeToString(account))
        } ?: let {
            log.info { "${usr}登录失败" }
            call.e401("密码错误")
        }
    }

    suspend fun changeProfile(call: ApplicationCall) {
        val params = call.receiveParameters()
        params["qq"]?.let { qq ->
            if (getByQQ(qq) != null) {
                call.e400("QQ用过了")
                return
            }
            accountCol.updateOne(equalById(call.uid), Updates.set("qq", qq))
        }
        params["name"]?.let { name ->
            if (getByName(name) != null) {
                call.e400("名字用过了")
                return
            }
            accountCol.updateOne(equalById(call.uid), Updates.set("name", name))
        }
        params["pwd"]?.let { pwd ->
            accountCol.updateOne(equalById(call.uid), Updates.set("pwd", pwd))
        }
        call.ok()
    }


}