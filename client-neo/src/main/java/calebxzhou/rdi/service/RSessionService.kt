package calebxzhou.rdi.service

import calebxzhou.rdi.ihq.net.IhqClient
import calebxzhou.rdi.log
import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftProfileTexture
import com.mojang.authlib.minecraft.MinecraftSessionService
import com.mojang.authlib.properties.Property
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.net.InetAddress

class RSessionService : MinecraftSessionService{
    override fun joinServer(profile: GameProfile?, authenticationToken: String?, serverId: String?) {

    }

    override fun hasJoinedServer(user: GameProfile?, serverId: String?, address: InetAddress?): GameProfile? {
        return null
    }

    override fun getTextures(
        profile: GameProfile,
        requireSecure: Boolean
    ): Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> {
        val uuid = profile.id
        val response = runBlocking {  IhqClient.send(HttpMethod.Get,"skin", listOf("uuid" to uuid.toString()))}
        if(response.status.isSuccess()){
            val data = runBlocking {  response.bodyAsText()} .split("\n")
            val skin = data.getOrNull(0)
            val cape = data.getOrNull(1)
            log.info("${profile.name}的皮肤: ${skin}")
            log.info("${profile.name}披风: ${cape}")
            val params = mutableMapOf<MinecraftProfileTexture.Type, MinecraftProfileTexture>()
            if(!skin.isNullOrBlank()){
                params += MinecraftProfileTexture.Type.SKIN to MinecraftProfileTexture(skin, mapOf())
            }
            if(!cape.isNullOrBlank()){
                params += MinecraftProfileTexture.Type.CAPE to MinecraftProfileTexture(cape, mapOf())
            }
            return params
        }else{
            return mapOf()
        }

    }

    override fun fillProfileProperties(profile: GameProfile, requireSecure: Boolean): GameProfile {
        return profile
    }

    override fun getSecurePropertyValue(property: Property): String {
        return property.value
    }
}