package calebxzhou.rdi.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class BlockRec(
    @BsonId val id: ObjectId,
                    val name: String,
    )
