package calebxzhou.rdi.serdes

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.bson.types.ObjectId
import java.util.*

/**
 * calebxzhou @ 2024-06-18 14:25
 */
val module = SerializersModule {
    contextual(ObjectId::class, ObjectIdSerializer)
    contextual(UUID::class, UUIDSerializer)
}

// Use the module with JSON configuration
val serdesJson = Json { serializersModule = module }