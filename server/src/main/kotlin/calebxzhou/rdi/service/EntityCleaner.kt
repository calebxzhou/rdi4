package calebxzhou.rdi.service

import calebxzhou.rdi.util.forEachEntity
import calebxzhou.rdi.util.mc
import net.minecraft.world.entity.item.FallingBlockEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.projectile.Projectile
import java.util.TimerTask

object EntityCleaner : TimerTask() {
    fun cleanRubbish() {
        forEachEntity { lvl, entity ->
            //清理没有nbt的掉落物
            /*if (entity is ItemEntity) {
                val preserve = entity.item.hasTag() || entity.item.isEnchanted || entity.hasCustomName()
                if (!preserve) {
                    lvl.getEntity(entity.id)?.discard()
                }
            }*/
            //清理下落方块
            if (entity is FallingBlockEntity) {
                entity.discard()
            }
            //清理投掷物
            if (entity is Projectile) {
                entity.discard()
            }

        }
    }

    fun cleanMobs() {
        forEachEntity { lvl, entity ->
            //清理怪物
            if (entity is Monster) {
                //保留：永久的 有名字的
                val needReserve = entity.isPersistenceRequired || entity.hasCustomName()
                if(!needReserve){
                    entity.discard()
                }

            }

        }
    }

    override fun run() {
        mc.execute {

            cleanMobs()
            cleanRubbish()
        }
    }
}