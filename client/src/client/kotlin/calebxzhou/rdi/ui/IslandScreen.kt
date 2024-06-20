package calebxzhou.rdi.ui

import calebxzhou.rdi.util.formatTimestamp
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.ihq.net.protocol.general.OkCPacket
import calebxzhou.rdi.ihq.net.protocol.island.IslandCreateSPacket
import calebxzhou.rdi.ihq.net.protocol.island.IslandInfoCPacket
import calebxzhou.rdi.ihq.net.protocol.island.IslandMySPacket
import calebxzhou.rdi.ui.component.RButton
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.util.dialogInfo
import calebxzhou.rdi.util.drawTextAtCenter
import net.minecraft.client.gui.GuiGraphics

class IslandScreen : RScreen("岛屿管理") {
    var packet: IslandInfoCPacket? = null
    lateinit var enterBtn : RButton
    lateinit var profileBtn : RButton
    lateinit var createBtn : RButton
    lateinit var memberBtn : RButton
    override fun init() {
        IhqClient.sendPacket(IslandMySPacket())
        enterBtn = RButton(0,height-20,60,"进岛"){

        }.also { registerWidget(it) }
        profileBtn = RButton(100,height-20,60,"个人信息"){}.also { registerWidget(it) }
        createBtn = RButton(200,height-20,60,"建岛"){
            IhqClient.sendPacket(IslandCreateSPacket())
        }.also { registerWidget(it) }
        memberBtn = RButton(300,height-20,60,"成员管理"){}.also { registerWidget(it) }
        super.init()
    }

    override fun doRender(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {

        packet?.run {
            drawTextAtCenter(guiGraphics, "岛屿ID ${id.toHexString()}", 50)
            drawTextAtCenter(guiGraphics, "名称 $name", 80)
            drawTextAtCenter(guiGraphics, "创建时间 ${formatTimestamp(createTime)}", 140)
            drawTextAtCenter(guiGraphics, "成员 ${members.joinToString(",")}", 170)
        }?: drawTextAtCenter(guiGraphics, "未获取到岛屿信息")
    }
    fun onResponse(packet: IslandInfoCPacket) {
        this.packet = packet
    }

    fun onOk(packet: OkCPacket) {
        dialogInfo(packet.msg)
    }
}