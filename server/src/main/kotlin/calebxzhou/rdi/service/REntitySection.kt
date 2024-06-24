package calebxzhou.rdi.service

import net.minecraft.util.AbortableIterationConsumer
import net.minecraft.util.ClassInstanceMultiMap
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.entity.EntityAccess
import net.minecraft.world.level.entity.EntitySection
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.level.entity.Visibility
import net.minecraft.world.phys.AABB
import java.util.stream.Stream

class REntitySection<T : EntityAccess>(entityClazz: Class<T>, var chunkStatus: Visibility) : EntitySection<T>(entityClazz,
    chunkStatus
) {
    private val storage = ClassInstanceMultiMap(entityClazz)
    private val typeCount = hashMapOf<Class<T>,Int>()
    private var itemCount = 0
    private var monsterCount = 0
    override fun add(entity: T) {
        val entityClass = entity.javaClass
        val currentCount = typeCount[entityClass] ?: 0
        if(entity is ItemEntity){
            itemCount++
            if(itemCount > 127) {
                //清除旧掉落物
                storage.find(ItemEntity::class.java)
                    .filter {
                        //有标签的 附魔的 自定义昵称的不清
                        !it.item.hasTag() && !it.item.isEnchanted && !entity.hasCustomName()
                    }
                    .maxBy { it.age }.discard()
            }
        }
        if(entity is Monster){
            monsterCount++
            if(monsterCount > 16) {
                //清除旧怪物
                storage.find(Monster::class.java)
                    .filter {
                        //有自定义昵称的不清
                        !entity.hasCustomName()
                    }
                    .forEach { it.discard() }
            }
        }

        typeCount[entityClass] = currentCount + 1
        storage .add(entity)
    }

    override fun remove(entity: T): Boolean {
        val result = storage .remove( entity)
        if (result) {
            val entityClass = entity.javaClass
            val currentCount = typeCount[entityClass] ?: return false
            if(entity is ItemEntity){
                itemCount--
            }
            if(entity is Monster){
                monsterCount--
            }
            if (currentCount > 1) {
                typeCount[entityClass] = currentCount - 1
            } else {
                typeCount.remove(entityClass)
            }
        }
        return result
    }



    override fun getEntities(
        bounds: AABB,
        consumer: AbortableIterationConsumer<T>
    ): AbortableIterationConsumer.Continuation {
        for (entityAccess in this.storage) {
            if (entityAccess.boundingBox.intersects(bounds) && consumer.accept(entityAccess).shouldAbort()) {
                return AbortableIterationConsumer.Continuation.ABORT
            }
        }

        return AbortableIterationConsumer.Continuation.CONTINUE
    }

    override fun <U : T> getEntities(
        test: EntityTypeTest<T, U>,
        bounds: AABB,
        consumer: AbortableIterationConsumer<in U>
    ): AbortableIterationConsumer.Continuation {
        val collection = this.storage.find(test.baseClass)
        if (collection.isEmpty()) {
            return AbortableIterationConsumer.Continuation.CONTINUE
        } else {
            for (entityAccess in collection) {
                val entityAccess2 = test.tryCast(entityAccess)
                if (entityAccess2 != null && entityAccess.boundingBox.intersects(bounds) && consumer.accept(
                        entityAccess2
                    ).shouldAbort()
                ) {
                    return AbortableIterationConsumer.Continuation.ABORT
                }
            }

            return AbortableIterationConsumer.Continuation.CONTINUE
        }
    }

    override fun isEmpty(): Boolean {
        return storage.isEmpty()
    }
    override fun getEntities(): Stream<T> {
        return storage.stream()
    }
    override fun getStatus(): Visibility {
        return chunkStatus
    }

    override fun updateChunkStatus(chunkStatus: Visibility): Visibility {
        val visibility = this.chunkStatus
        this.chunkStatus = chunkStatus
        return visibility
    }

    override fun size(): Int {
        return storage.size
    }
}