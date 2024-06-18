package calebxzhou.rdi.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.*

@Serializable
data class RAccount(
    @Contextual
    @BsonId val id: ObjectId,
    @Contextual val uuid: UUID,
    val name: String,
    val pwd: String,
    val qq: String,
    val score: Int = 0,
    val skin: String = "",
    val cape: String = "",
) {
    companion object {
        var now: RAccount? = null
    }
}