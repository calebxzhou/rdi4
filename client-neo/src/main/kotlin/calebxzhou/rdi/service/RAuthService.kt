package calebxzhou.rdi.service

import com.mojang.authlib.*
import com.mojang.authlib.minecraft.MinecraftSessionService
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import java.net.Proxy

class RAuthService : YggdrasilAuthenticationService(Proxy.NO_PROXY) {
    override fun createUserAuthentication(agent: Agent?): UserAuthentication {
        return RUserAuthService()
    }

    override fun createMinecraftSessionService(): MinecraftSessionService {
        return RSessionService()
    }

    override fun createProfileRepository(): GameProfileRepository? {
        return null
    }

}