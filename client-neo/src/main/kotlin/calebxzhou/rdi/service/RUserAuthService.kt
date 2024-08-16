package calebxzhou.rdi.service

import calebxzhou.rdi.model.Account
import com.mojang.authlib.GameProfile
import com.mojang.authlib.UserAuthentication
import com.mojang.authlib.UserType
import com.mojang.authlib.properties.PropertyMap

class RUserAuthService : UserAuthentication {
    override fun canLogIn(): Boolean {
        return true
    }

    override fun logIn() {
    }

    override fun logOut() {
    }

    override fun isLoggedIn(): Boolean {
        return false
    }

    override fun canPlayOnline(): Boolean {
        return true
    }

    override fun getAvailableProfiles(): Array<GameProfile?> {
        return arrayOf(Account.now?.profile)
    }

    override fun getSelectedProfile(): GameProfile? {
        return Account.now?.profile
    }

    override fun selectGameProfile(profile: GameProfile?) {
    }

    override fun loadFromStorage(credentials: MutableMap<String, Any>?) {
    }

    override fun saveForStorage(): MutableMap<String, Any>? {
        return null
    }

    override fun setUsername(username: String?) {
    }

    override fun setPassword(password: String?) {

    }

    override fun getAuthenticatedToken(): String? {
       return null
    }

    override fun getUserID(): String? {
        return null
    }

    override fun getUserProperties(): PropertyMap? {
        return null
    }

    override fun getUserType(): UserType {
        return UserType.MOJANG
    }
}