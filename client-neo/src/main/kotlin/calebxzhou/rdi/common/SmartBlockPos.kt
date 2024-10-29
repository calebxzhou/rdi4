package calebxzhou.rdi.common

import net.minecraft.core.BlockPos
fun bos(x:Int=0,y:Int=0,z:Int=0): SmartBlockPos{
    return SmartBlockPos(x, y, z)
}
data class SmartBlockPos(private val x:Int=0,private val y:Int=0,private val z:Int=0) : BlockPos(x,y,z){
    infix fun dx(value: Int) = SmartBlockPos(x+value,y,z)
    infix fun dy(value: Int) = SmartBlockPos(x,y+value,z)
    infix fun dz(value: Int) = SmartBlockPos(x,y,z+value)
    val copy
        get() = SmartBlockPos(x,y,z)

}
