package calebxzhou.rdi.mixin.client.tfc;

import net.dries007.tfc.common.blockentities.InventoryBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InventoryBlockEntity.class)
public interface AInventoryBlockEntity<C extends IItemHandlerModifiable & INBTSerializable<CompoundTag>> {
    @Accessor
    C getInventory();

}
