package calebxzhou.rdi.common

import net.minecraft.core.BlockPos
import thedarkcolour.kotlinforforge.forge.vectorutil.v3d.minus
import thedarkcolour.kotlinforforge.forge.vectorutil.v3d.plus

fun bos(x: Int = 0, y: Int = 0, z: Int = 0): SmartBlockPos {
    return SmartBlockPos(x, y, z)
}

val BlockPos.smart
    get() = SmartBlockPos(x, y, z)

data class SmartBlockPos(private val x: Int = 0, private val y: Int = 0, private val z: Int = 0) : BlockPos(x, y, z) {
    infix fun dx(value: Int) = SmartBlockPos(x + value, y, z)
    infix fun dy(value: Int) = SmartBlockPos(x, y + value, z)
    infix fun dz(value: Int) = SmartBlockPos(x, y, z + value)

    val east
        get() = this dx 1
    val west get() = this dx -1
    val north get() = this dz -1
    val south get() = this dz 1

    val above get() = this dy 1
    val below get() = this dy -1

    /*
    XXX
    X0X
    XXX
     */
    val around
        get() = arrayOf(
            this.east,
            this.south,
            this.west,
            this.north,
            this.east.north,
            this.east.south,
            this.west.north,
            this.west.south
        )

    /*
     XXXXX
     XXXXX
     XX0XX
     XXXXX
     XXXXX */
    val around2
        get() = arrayOf(
            this.east,
            this.east.north,
            this.east.north.north,
            this.east.east,
            this.east.east.north,
            this.east.east.north.north,
            this.east.east.south,
            this.east.east.south.south,
            this.east.south,
            this.east.south.south,
            this.west,
            this.west.north,
            this.west.north.north,
            this.west.west,
            this.west.west.north,
            this.west.west.north.north,
            this.west.west.south,
            this.west.west.south.south,
            this.west.south,
            this.west.south.south,
            this.north,
            this.north.east,
            this.north.east.east,
            this.north.west,
            this.north.west.west,
            this.north.north,
            this.south,
            this.south.east,
            this.south.east.east,
            this.south.west,
            this.south.west.west,
            this.south.south
        )
    val copy
        get() = SmartBlockPos(x, y, z)

}

class SmartBlockPoses {
    val poses = mutableListOf<SmartBlockPos>()

}
/*
boses(origin){
    x
    x

}
 */