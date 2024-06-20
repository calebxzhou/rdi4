package calebxzhou.rdi.service

import calebxzhou.rdi.db
import calebxzhou.rdi.model.RAccount
import calebxzhou.rdi.util.nickname
import calebxzhou.rdi.util.ok
import calebxzhou.rdi.util.preventLogin
import com.mojang.authlib.GameProfile
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.minecraft.network.Connection
import net.minecraft.network.protocol.login.ServerboundHelloPacket
import net.minecraft.server.level.ServerPlayer
import org.bson.types.ObjectId
import java.nio.charset.StandardCharsets
import java.util.*

object AccountService {
    private val dbcl = db.getCollection<RAccount>("account")

    //根据qq获取
    private suspend fun getByQQ(qq: String): RAccount? = dbcl.find(eq("qq", qq)).firstOrNull()

    //根据uuid获取
    suspend fun getByUuid(uuid: UUID): RAccount? = dbcl.find(eq("uuid", uuid)).firstOrNull()

    @JvmStatic
    fun createUuid(name: String): UUID {
        return UUID.nameUUIDFromBytes(name.toByteArray(StandardCharsets.UTF_8));
    }

    //登录
    fun onLogin(pack: ServerboundHelloPacket, connection: Connection): GameProfile? {
        val uuid = pack.profileId?.get() ?: let {
            connection.preventLogin("RDI登录协议错误001")
            return null
        }

        val name = pack.name.split("\n").getOrNull(0) ?: let {
            connection.preventLogin("RDI登录协议错误002")
            return null
        }
        val pwd = pack.name.split("\n").getOrNull(1) ?: ""
        val player = runBlocking {
            getByUuid(uuid)
        }

        if (player == null) {
            connection.preventLogin("没注册")
            return null
        }
        if (player.pwd != pwd) {
            connection.preventLogin("密码错误")
            return null
        }
        return GameProfile(uuid,name)
    }

    //设定密码
    fun ServerPlayer.setPassword(pwd: String) {
        GlobalScope.launch {

            var account = getByUuid(uuid)
            if (account == null) {
                account = RAccount(ObjectId(), uuid, nickname, pwd, "")
                dbcl.insertOne(account)
            } else {
                dbcl.updateOne(eq("uuid", uuid), Updates.set("pwd", pwd))
            }
            ok("设定密码为$pwd")
        }

    }
}