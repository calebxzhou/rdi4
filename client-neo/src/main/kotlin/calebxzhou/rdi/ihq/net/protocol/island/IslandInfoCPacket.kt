package calebxzhou.rdi.ihq.net.protocol.island

import calebxzhou.craftcone.utils.ByteBufUtils.readObjectId
import calebxzhou.craftcone.utils.ByteBufUtils.readString
import calebxzhou.rdi.ihq.net.protocol.CPacket
import calebxzhou.rdi.ui.IslandScreen
import calebxzhou.rdi.util.mc
import io.netty.buffer.ByteBuf
import org.bson.types.ObjectId

data class IslandInfoCPacket(val id:ObjectId,val name:String,val createTime: Long,val members: List<String>):  CPacket{
    constructor(buf: ByteBuf): this(buf.readObjectId(),buf.readString(),buf.readLong(),buf.readString().split("@"))

    override fun process() {
        val screen = mc.screen
        if(screen is IslandScreen){

        }
    }
}
