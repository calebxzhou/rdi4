package calebxzhou.rdi.ihq.test

import calebxzhou.rdi.Const
import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.ihq.net.protocol.account.LoginSPacket

/**
 * calebxzhou @ 2024-06-07 22:36
 */
fun main() {
    IhqClient.sendPacket(LoginSPacket("123","123"))
}