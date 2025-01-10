package calebxzhou.rdi.model

import calebxzhou.rdi.Const
import calebxzhou.rdi.Const.VERSION_DISP
import calebxzhou.rdi.ihq.HttpMethod
import calebxzhou.rdi.ihq.IhqClient
import calebxzhou.rdi.ihq.protocol.account.LoginSPacket
import calebxzhou.rdi.ihq.protocol.account.RegisterSPacket
import calebxzhou.rdi.serdes.serdesJson
import calebxzhou.rdi.sound.RSoundPlayer
import calebxzhou.rdi.ui.component.REditBoxValidationResult
import calebxzhou.rdi.ui.component.RFormScreenSubmitHandler
import calebxzhou.rdi.ui.component.RScreen
import calebxzhou.rdi.ui.component.formScreen
import calebxzhou.rdi.ui.general.ROption
import calebxzhou.rdi.ui.general.alert
import calebxzhou.rdi.ui.general.alertErr
import calebxzhou.rdi.ui.general.alertOs
import calebxzhou.rdi.ui.general.optionScreen
import calebxzhou.rdi.ui.screen.RProfileScreen
import calebxzhou.rdi.ui.screen.RTitleScreen
import calebxzhou.rdi.util.LocalStorage
import calebxzhou.rdi.util.bodyText
import calebxzhou.rdi.util.goHome
import calebxzhou.rdi.util.goScreen
import calebxzhou.rdi.util.mc
import calebxzhou.rdi.util.mcMainThread
import calebxzhou.rdi.util.supportIPv6
import calebxzhou.rdi.util.toastOk
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.resolver.ServerAddress
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import kotlin.coroutines.CoroutineContext

data class RServer(val ip: String, val gamePort: Int, val hqPort: Int) {
    val mcData= ServerData("RDI", ip,false)
    companion object {
        val OFFICIAL_KWL = RServer("kwl1.calebxzhou.cn", 28501, 28502)
        val OFFICIAL_DEBUG = RServer("127.0.0.1", 38430, 38411)
        val OFFICIAL_NNG = RServer("rdi.calebxzhou.cn", 38430, 38411)

        var now: RServer? = null

        val serverSelectScreen: RScreen
            get() = optionScreen(RTitleScreen(), "选择服务器") {
                this += ROption("KWL", true, "桂林联通\n感谢科洛提供的服务器") {
                    OFFICIAL_KWL.connect()
                }
                this += ROption("NNG", supportIPv6, "南宁电信\n暂时仅支持IPv6") {
                    OFFICIAL_NNG.connect()
                }
                if (Const.DEBUG) {
                    this += ROption("本地调试", true, " ") {
                        OFFICIAL_DEBUG.connect()
                    }
                }
            }

        val loginScreen: RScreen
            get() = formScreen(serverSelectScreen, "登录账号") {
                text("usr", "QQ号/昵称/ID", 16, defaultValue = LocalStorage["usr"])
                pwd("pwd", "密码", defaultValue = LocalStorage["pwd"])
                bottomLayoutBuilder = {
                    iconButton("ssp", text = "注册新号") { mc goScreen regScreen }
                    iconButton("ethernet", text = "局域网") { mc goScreen lanScreen }
                }
                submit {
                    now?.playerLogin(it)
                }
            }
        val lanScreen: RScreen
            get() = formScreen(loginScreen, "输入信息") {
                text("name", "你的游戏昵称", 16, defaultValue = LocalStorage["guestName"])
                submit {
                    val name = it.formData["name"]!!
                    LocalStorage["guestName"] = name
                    RAccount.guestLogin(name)
                    mc goScreen JoinMultiplayerScreen(RTitleScreen())
                    alert("0.让你的朋友打开一个存档，输入/lan指令启动联机\n1.然后这个界面，就能搜到他了\n如果搜不到，可以手动输入他的ip跟端口添加")
                }
            }


        val regScreen: RScreen
            get() = formScreen(loginScreen, "注册账号") {
                text("name", "昵称", 16) {
                    if (it.value.length in 3..16) {
                        REditBoxValidationResult(true)
                    } else {
                        REditBoxValidationResult(false, "昵称长度必须3~16")

                    }
                }
                text("qq", "QQ号", 10, true) {
                    if (it.value.length in 5..10) {
                        REditBoxValidationResult(true)
                    } else {
                        REditBoxValidationResult(false, "QQ格式错误")
                    }
                }
                pwd("pwd", "密码")
                pwd("cpwd", "确认密码")
                submit {
                    now?.playerRegister(it)
                }

            }


    }

    fun disconnect() {
        now = null
        mc.goHome()
    }

    fun connect() {
        hqSend(path = "version") {
            if (it.entity.bodyText == Const.VERSION_STR) {

                now = this
                mc goScreen loginScreen
            } else {
                error("版本不符，你${Const.VERSION_STR}，服务端${it.entity.bodyText}")
            }
        }
    }

    fun playerRegister(it: RFormScreenSubmitHandler) {
        val pwd = it.formData["pwd"]!!
        val cpwd = it.formData["cpwd"]!!
        if (pwd != cpwd) {
            alertOs("确认密码与密码不一致")
            return
        }
        val qq = it.formData["qq"]!!
        val name = it.formData["name"]!!
        hqSend(
            HttpMethod.POST,
            "register",
            "name" to name,
            "pwd" to pwd,
            "qq" to qq
        ) {
            toastOk("注册成功")
            LocalStorage += "usr" to qq
            LocalStorage += "pwd" to pwd
            mc goScreen loginScreen
        }

    }

    fun playerLogin(it: RFormScreenSubmitHandler) {
        val usr = it.formData["usr"]!!
        val pwd = it.formData["pwd"]!!
        hqSend(
            HttpMethod.POST,
            "login",
            params = arrayOf( "usr" to usr, "pwd" to pwd ),
        ) {
            val account = serdesJson.decodeFromString<RAccount>(it.entity.bodyText)
            LocalStorage += "usr" to usr
            LocalStorage += "pwd" to pwd
            RAccount.now = account
            toastOk("登录成功")
            mc goScreen RProfileScreen(account,this)
        }
    }

    val scope = CoroutineScope(Dispatchers.IO)
    val hqClient = HttpClients.createDefault()

    fun hqSend(
        method: HttpMethod = HttpMethod.GET,
        path: String,
        vararg params: Pair<String, String>,
        onOk: (CloseableHttpResponse) -> Unit
    ) = scope.launch(CoroutineExceptionHandler { _, exception ->
        alertErr(exception.message ?: "未知错误")
        exception.printStackTrace()
    }) {
        val fullUrl = "http://${ip}:${hqPort}/${path}"
        val request: HttpUriRequest = when (method) {
            HttpMethod.POST -> {
                val post = HttpPost(fullUrl)
                val form = UrlEncodedFormEntity(params.map { BasicNameValuePair(it.first, it.second) })
                post.entity = form
                post
            }

            HttpMethod.GET -> {
                val get = HttpGet(fullUrl)
                val uriBuilder = URIBuilder(get.uri)
                params.forEach { uriBuilder.addParameter(it.first, it.second) }
                get.uri = uriBuilder.build()
                get
            }

            else -> throw IllegalArgumentException("Unsupported request type $method")
        }


        hqClient.execute(request).use { response ->
            if (response.statusLine.statusCode in 200..299) {
                onOk(response)
            } else {
                alertErr("${response.statusLine.statusCode}:${EntityUtils.toString(response.entity)}")
            }
        }
    }

}
