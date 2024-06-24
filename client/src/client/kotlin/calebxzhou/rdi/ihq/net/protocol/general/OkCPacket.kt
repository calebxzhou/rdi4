package calebxzhou.rdi.ihq.net.protocol.general

import calebxzhou.rdi.ui.IslandScreen
import calebxzhou.rdi.util.mc
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-08-15,8:47.
 */
//处理成功&数据
class OkCPacket(buf: ByteBuf): MessageCPacket(buf) {
    override fun process() {
        val screen = mc.screen



        if(screen is IslandScreen){
            //screen.onOk(this)
        }
    }
}