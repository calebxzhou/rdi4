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
data class RAccount(
    @Contextual
    @BsonId val id: ObjectId,
    @Contextual val uuid: UUID,
    var name: String,
    var pwd: String,
    var qq: String,
    val score: Int = 0,
    var skin: String?,// = "https://pic.imgdb.cn/item/6673efdcd9c307b7e9bc8a62.png",
    var cape: String?,// = "https://pic.imgdb.cn/item/6673f02ed9c307b7e9bd395c.png",
) {
    companion object {
        val DEFAULT = RAccount(ObjectId(), UUID.randomUUID(),"未登录","123456","12345",0,null,null)
        @JvmStatic
        var now: RAccount? = null

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