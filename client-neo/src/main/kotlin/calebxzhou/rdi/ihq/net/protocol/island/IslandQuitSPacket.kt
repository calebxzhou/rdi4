package calebxzhou.rdi.ihq.net.protocol.island

import calebxzhou.rdi.ihq.net.protocol.SPacket
import io.netty.buffer.ByteBuf

/**
 * Created  on 2023-07-06,8:48.
 */
//玩家退出岛屿
class IslandQuitSPacket: SPacket {
    override fun write(buf: ByteBuf) {
    }

}