package calebxzhou.rdi.hardware

import oshi.SystemInfo
import oshi.hardware.CentralProcessor
import oshi.hardware.ComputerSystem
import oshi.hardware.Display
import oshi.hardware.GraphicsCard
import oshi.hardware.PhysicalMemory
import oshi.software.os.OperatingSystem

object HardwareInfo {
    val system: ComputerSystem
    //val brand: String
    //val board: String
    val os: OperatingSystem
    val osArch = System.getProperty("os.arch")
    val osVersion = System.getProperty("os.version")
    val cpu: CentralProcessor
    val gpus: List<GraphicsCard>
    val mems: List<PhysicalMemory>
    val totalMemory: Long
    val displays: List<Display>
    //val cpu: String
    init {
        val systemInfo = SystemInfo()
        val hal = systemInfo.hardware
        system = hal.computerSystem
        //brand = "${csys.manufacturer}:${csys.model}"
        //val baseboard = csys.baseboard
        //board = "${baseboard.manufacturer}:${baseboard.model}"

        os = systemInfo.operatingSystem

        //os = "${osInfo.manufacturer} ${osInfo.family} ${osInfo.versionInfo}(${osArch},${osVersion})"

        cpu = hal.processor
        /*val cpuid = cpuinfo.processorIdentifier
        val cpuName = cpuid.name.replace("  ", "")
        val cpuCores = cpuinfo.physicalProcessorCount
        val cpuThreads = cpuinfo.logicalProcessorCount
        val cpuFreq = "%.2f".format((cpuid.vendorFreq / 1.0E9f).toDouble())
        val cpuMaxFreq = "%.2f".format(((cpuinfo.currentFreq).max() / 1.0E9f).toDouble())
        cpu = "${cpuName}(${cpuCores}C/${cpuThreads}T)@${cpuFreq}/${cpuMaxFreq}GHz"*/

        gpus = hal.graphicsCards
        /*for (gpuinfo in hal.graphicsCards) {
            val gpuVram = "%.2f".format(gpuinfo.vRam.toFloat() / (1024 * 1024 * 1024f))
            gpu.append("${gpuinfo.name} (${gpuVram}GB,${gpuinfo.vendor});")
        }*/

        mems = hal.memory.physicalMemory
        totalMemory = mems.sumOf { it.capacity }
       /* var memTotalSize = 0f
        for (meminfo in hal.memory.physicalMemory) {
            val memSizef = meminfo.capacity.toFloat() / (1024 * 1024 * 1024f)
            memTotalSize += memSizef
            val memSize = "%.2f".format(memSizef)
            val memType = meminfo.memoryType
            val memSpd = (meminfo.clockSpeed / 1.0E6f).toInt().toString()
            mem.append("${memSize}GB-${memType}-${memSpd};")
        }
        mem.append("(∑%.2fGB)".format(memTotalSize))*/
        displays = hal.displays
    }

    override fun toString(): String {
        val cpuid = cpu.processorIdentifier
        val cpuName = cpuid.name
        val cpuMaxFreq = "%.2f".format((cpuid.vendorFreq / 1.0E9f).toDouble())
        val cpuCores = cpu.physicalProcessorCount
        val cpuThreads = cpu.logicalProcessorCount
        val gpuInfo =
            gpus.joinToString("\n") {
                "${it.name} ${"%.2f".format(it.vRam.toFloat() / (1024 * 1024 * 1024f))}GB"
            }
        return """${os.manufacturer} ${os.family} ${os.versionInfo}
$cpuName ${cpuCores}c${cpuThreads}t ${cpuMaxFreq}GHz
$gpuInfo
${mems[0].memoryType}-${(mems.map { it.clockSpeed }.average() / 1.0e6f).toInt()} ${mems.sumOf { it.capacity } /1024/1024/1024}GB
        """.trimMargin().replace(Regex(" +"), " ")
    }
}