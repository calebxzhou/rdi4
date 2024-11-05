package calebxzhou.rdi.hardware

import jogamp.graph.font.typecast.ot.table.Table.name
import oshi.SystemInfo
import oshi.util.EdidUtil
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.awt.HeadlessException
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * calebxzhou @ 2024-11-04 23:12
 */
val systemInfo = SystemInfo()
val hal = systemInfo.hardware
val os = systemInfo.operatingSystem.run { "$manufacturer $family $versionInfo" }
val cpu =
    hal.processor.run { "${processorIdentifier.name} ${"%.2f".format(processorIdentifier.vendorFreq / 1.0E9)}GHz ${physicalProcessorCount}C${logicalProcessorCount}T" }
        .replace(Regex(" +")," ")
val gpu = hal.graphicsCards.joinToString { "${it.name} ${it.vRam / 1024 / 1024 / 1024}GB;" }
val mem =
    hal.memory.physicalMemory.run { "${sumOf { it.capacity } / 1024 / 1024 / 1024}GB ${this[0].memoryType}-${(minBy { it.clockSpeed }.clockSpeed / 1.0e6).toInt()}" }
val disp: List<String>
    get() {
        val devices = try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices//.map { "${it.iDstring} ${it.displayMode.width}x${it.displayMode.height}@${it.displayMode.refreshRate}Hz" }
        } catch (e: HeadlessException) {
            null
        }
        return hal.displays.map{it.edid}.mapIndexed { i, it ->
            val name = EdidUtil.getDescriptors(it).firstNotNullOfOrNull {
                when (EdidUtil.getDescriptorType(it)) {
                    0xfc -> EdidUtil.getDescriptorText(it)
                    else -> null
                }
            } ?:""
            val hcm = EdidUtil.getHcm(it).toFloat()
            val vcm = EdidUtil.getVcm(it).toFloat()
            val inchHypo = (sqrt(hcm.pow(2) + vcm.pow(2)) * 0.393701).roundToInt()
            val mode = devices?.getOrNull(i)?.displayMode?.run { "${width}x${height}@${refreshRate}Hz" }?:""
            "$name $mode ${hcm}x${vcm}cm(${inchHypo}寸)"
        }
    }