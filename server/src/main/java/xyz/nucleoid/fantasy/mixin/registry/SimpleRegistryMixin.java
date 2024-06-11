package xyz.nucleoid.fantasy.mixin.registry;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.nucleoid.fantasy.RemoveFromRegistry;

import java.util.List;
import java.util.Map;

import net.minecraft.core.Holder.Reference;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@Mixin(MappedRegistry.class)
public abstract class SimpleRegistryMixin<T> implements RemoveFromRegistry<T> {

    @Shadow @Final private Map<T, Reference<T>> byValue;

    @Shadow @Final private Map<ResourceLocation, Reference<T>> byLocation;

    @Shadow @Final private Map<ResourceKey<T>, Reference<T>> byKey;

    @Shadow @Final private Map<T, Lifecycle> lifecycles;

    @Shadow @Final private ObjectList<Reference<T>> byId;

    @Shadow @Final private Object2IntMap<T> toId;


    @Shadow private boolean frozen;

    @Shadow @Nullable private List<Reference<T>> holdersInOrder;

    @Override
    public boolean fantasy$remove(T entry) {
        var registryEntry = this.byValue.get(entry);
        int rawId = this.toId.removeInt(entry);
        if (rawId == -1) {
            return false;
        }

        try {
            this.byId.set(rawId, null);
            this.byLocation.remove(registryEntry.key().location());
            this.byKey.remove(registryEntry.key());
            this.lifecycles.remove(entry);
            this.byValue.remove(entry);
            if (this.holdersInOrder != null) {
                this.holdersInOrder.remove(registryEntry);
            }

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean fantasy$remove(ResourceLocation key) {
        var entry = this.byLocation.get(key);
        return entry != null && entry.isBound() && this.fantasy$remove(entry.value());
    }

    @Override
    public void fantasy$setFrozen(boolean value) {
        this.frozen = value;
    }

    @Override
    public boolean fantasy$isFrozen() {
        return this.frozen;
    }
}
