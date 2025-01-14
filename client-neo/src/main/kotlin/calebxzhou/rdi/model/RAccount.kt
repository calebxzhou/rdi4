package calebxzhou.rdi.model

import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.objectId
import calebxzhou.rdi.util.toUUID
import com.mojang.authlib.GameProfile
import com.possible_triangle.sliceanddice.block.slicer.SlicerArmInteractionType.id
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
    var name: String,
    var pwd: String,
    var qq: String,
    val score: Int = 0,
    var cloth: Cloth ?= null,
    ) {
    @Serializable
    data class Cloth(
        val isSlim :Boolean = false,
        var skin: String?=null,
        var cape: String?=null
    )

    companion object {
        val DEFAULT = RAccount(ObjectId(),  "未登录","123456","12345",0,null, )
        @JvmStatic
        var now: RAccount? = null

        @JvmStatic
        val mcUserNow
            get() = now?.mcUser ?: User(
            "rdi",
            UUID.randomUUID().toString(),
            "",
            Optional.empty(),
            Optional.empty(),
            User.Type.MOJANG
        )
        val isLogin
            get() = now != null

        fun guestLogin(name: String){
            RAccount.now = RAccount(name.objectId,name,"","")
        }
    }
    @Contextual
    val uuid = id.toUUID()
    @Transient
    val profile = GameProfile(uuid, name)

    @Transient
    val mcUser = User(name,uuid.toString(), "", Optional.empty(), Optional.empty(), User.Type.MOJANG)
    val skinLocation
        get() = mc.skinManager.getInsecureSkinLocation(profile)
    fun logout() {
        now = null
    }
}