package calebxzhou.rdi.service

import calebxzhou.rdi.db
import calebxzhou.rdi.exception.RAccountException
import calebxzhou.rdi.model.RPlayer
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

object PlayerService {
    private val dbcl = db.getCollection<RPlayer>("player")

    //根据qq获取
    private suspend fun getByQQ(qq: String): RPlayer? = dbcl.find(eq("qq", qq)).firstOrNull()
    //根据rid获取
    suspend fun getById(id: ObjectId): RPlayer? = dbcl.find(eq("_id", id)).firstOrNull()
    /*//登录
    suspend fun checkPassword(pack: LoginSPacket):RPlayer{
        val player = getByQQ(pack.qq)
        if(player==null || player.pwd != pack.pwd){
            throw RAccountException("密码错误")
        }
        return player
    }
    //注册
    suspend fun register(pack: RegisterSPacket) {
        getByQQ(pack.qq)?.let {
            throw RAccountException("账号已被注册")
        } ?: let{
            if(!pack.name.matches(Regex("^[A-Za-z0-9_]+$")))
                throw RAccountException("昵称只能由字母和数字组成")
            dbcl.insertOne(RPlayer(ObjectId(), pack.name, pack.pwd, pack.qq))
            throw RAccountException("注册成功，请重新登录")
        }
    }*/
}