package xyz.nucleoid.fantasy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nucleoid.fantasy.FantasyWorldAccess;

import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin implements FantasyWorldAccess {
    @Unique
    private static final int TICK_TIMEOUT = 20 * 15;

    @Unique
    private boolean fantasy$tickWhenEmpty = true;
    @Unique
    private int fantasy$tickTimeout;

    @Shadow
    public abstract List<ServerPlayer> players();

    @Shadow
    public abstract ServerChunkCache getChunkSource();

    @Override
    public void fantasy$setTickWhenEmpty(boolean tickWhenEmpty) {
        this.fantasy$tickWhenEmpty = tickWhenEmpty;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        boolean shouldTick = this.fantasy$tickWhenEmpty || !this.isWorldEmpty();
        if (shouldTick) {
            this.fantasy$tickTimeout = TICK_TIMEOUT;
        } else if (this.fantasy$tickTimeout-- <= 0) {
            ci.cancel();
        }
    }

    @Override
    public boolean fantasy$shouldTick() {
        boolean shouldTick = this.fantasy$tickWhenEmpty || !this.isWorldEmpty();
        return shouldTick || this.fantasy$tickTimeout > 0;
    }

    @Unique
    private boolean isWorldEmpty() {
        return this.players().isEmpty() && this.getChunkSource().getLoadedChunksCount() <= 0;
    }

    @Redirect(
            method = "advanceWeatherCycle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"
            )

    )
    private void advanceWeatherCycle(PlayerList instance, Packet<?> packet) {
        // Vanilla sends rain packets to all players when rain starts in a world,
        // even if they are not in it, meaning that if it is possible to rain in the world they are in
        // the rain effect will remain until the player changes dimension or reconnects.
        instance.broadcastAll(packet, this.getChunkSource().getLevel().dimension());
    }
}
