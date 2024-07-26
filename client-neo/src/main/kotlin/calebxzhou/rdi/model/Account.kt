package calebxzhou.rdi.model

import calebxzhou.rdi.util.mc
import com.mojang.authlib.GameProfile
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.client.User
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.*

@Serializable
data class Account(
    @Contextual
    @BsonId val id: ObjectId,
    @Contextual val uuid: UUID,
    var name: String,
    var pwd: String,
    var qq: String,
    val score: Int = 0,
    val cloth: Cloth ?= null,
    ) {
    @Serializable
    data class Cloth(
        val isSlim :Boolean = false,
        var skin: String,
        var cape: String,
        var elytra: String,
    )

    companion object {
        val DEFAULT = Account(ObjectId(), UUID.randomUUID(),"未登录","123456","12345",0,null, )
        @JvmStatic
        var now: Account? = null

        @JvmStatic
        var mcUserNow = now?.mcUser ?: User(
            "rdi",
            UUID.randomUUID().toString(),
            "",
            Optional.empty(),
            Optional.empty(),
            User.Type.MOJANG
        )
        val isLogin
            get() = now != null
    }

    @Transient
    val profile = GameProfile(uuid, name)

    @Transient
    val mcUser = User(name, uuid.toString(), "", Optional.empty(), Optional.empty(), User.Type.MOJANG)
    val skinLocation
        get() = mc.skinManager.getInsecureSkinLocation(profile)
}