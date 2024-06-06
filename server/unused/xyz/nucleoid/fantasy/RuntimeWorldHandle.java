package xyz.nucleoid.fantasy;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class RuntimeWorldHandle {
    private final Fantasy fantasy;
	public final IslandLevel ilvl;

    public RuntimeWorldHandle(Fantasy fantasy, IslandLevel ilvl) {
        this.fantasy = fantasy;
        this.ilvl = ilvl;
    }
	public void setTickWhenEmpty(boolean tickWhenEmpty) {
        ((FantasyWorldAccess) this.asWorld()).fantasy$setTickWhenEmpty(tickWhenEmpty);
    }

    public void delete() {
        this.fantasy.enqueueWorldDeletion(this.ilvl);
    }
    public ServerLevel asWorld() {
        return this.ilvl;
    }


    public ResourceKey<Level> getRegistryKey() {
        return this.ilvl.dimension();
    }
}
