package calebxzhou.rdi.ihq.service

import calebxzhou.rdi.ihq.util.isValidObjectId
import calebxzhou.rdi.ihq.util.scope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bson.types.ObjectId

typealias TsgtHandler = suspend () -> TsgtResult

data class TsgtResult(val isSuccess: Boolean, val reason: String = ""){
    companion object{
        val INVALID = TsgtResult(false, "无效事务")
        fun success(reason: String): TsgtResult {
            return TsgtResult(true,reason)
        }
    }
}
object Transergeant {

    private val trans = hashMapOf<ObjectId, TsgtHandler>()

    fun add(handler: TsgtHandler): ObjectId {
        val id = ObjectId()
        trans += id to handler
        return id
    }
    //xx秒后失效
    fun add(validSeconds: Long,handler: TsgtHandler): ObjectId {
        val id = add(handler)
        scope.launch {
            delay(validSeconds*1000)
            trans -= id
        }
        return id
    }

    suspend fun invoke(id: String): TsgtResult {
        if (!id.isValidObjectId()) {
            return TsgtResult.INVALID
        }
        val tsid = ObjectId(id)
        return trans[tsid]?.invoke()?.let {
            trans -= tsid
            it
        } ?: TsgtResult.INVALID
    }
    /*operator fun get(id: String): TsgtHandler?{
        if(!id.isValidObjectId()){
            log.warn {  "无效的事务ID" }
            return null
        }
        return trans[ObjectId(id)]
    }*/
}
