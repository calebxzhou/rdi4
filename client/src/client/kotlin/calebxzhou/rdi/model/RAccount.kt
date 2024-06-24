package calebxzhou.rdi.model

import calebxzhou.rdi.util.mc
import com.mojang.authlib.GameProfile
import io.ktor.client.plugins.auth.providers.*
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
    var skin: String = "",
    var cape: String = "",
) {
    companion object {
        @JvmStatic
        var now: RAccount? = null
        val ihqCredential
            get() = BasicAuthCredentials(now?.id.toString(), now?.pwd ?: "")

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