package calebxzhou.rdi.launcher

/**
 * calebxzhou @ 2024-06-29 16:52
 */
fun main(){
    val dir = System.getProperty("user.dir")
    println("启动游戏文件夹：$dir")
    val java = "$dir\\jre\\bin\\java.exe"
    val params2 = """-Djava.library.path="${dir}\.minecraft\versions\MC1.20.1\MC1.20.1-natives"    
         -Djna.tmpdir="${dir}\.minecraft\versions\MC1.20.1\MC1.20.1-natives"    
         -Dorg.lwjgl.system.SharedLibraryExtractPath="${dir}\.minecraft\versions\MC1.20.1\MC1.20.1-natives"      
         -Dio.netty.native.workdir="${dir}\.minecraft\versions\MC1.20.1\MC1.20.1-natives"    
         -Dminecraft.launcher.brand=RDI
         -Dminecraft.launcher.version=326
        -cp 
        "${dir}\.minecraft\libraries\com\github\oshi\oshi-core\6.2.2\oshi-core-6.2.2.jar;${dir}\.minecraft\libraries\com\google\code\gson\gson\2.10\gson-2.10.jar;${dir}\.minecraft\libraries\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;${dir}\.minecraft\libraries\com\google\guava\guava\31.1-jre\guava-31.1-jre.jar;${dir}\.minecraft\libraries\com\ibm\icu\icu4j\71.1\icu4j-71.1.jar;${dir}\.minecraft\libraries\com\mojang\authlib\4.0.43\authlib-4.0.43.jar;${dir}\.minecraft\libraries\com\mojang\blocklist\1.0.10\blocklist-1.0.10.jar;${dir}\.minecraft\libraries\com\mojang\brigadier\1.1.8\brigadier-1.1.8.jar;${dir}\.minecraft\libraries\com\mojang\datafixerupper\6.0.8\datafixerupper-6.0.8.jar;${dir}\.minecraft\libraries\com\mojang\logging\1.1.1\logging-1.1.1.jar;${dir}\.minecraft\libraries\com\mojang\patchy\2.2.10\patchy-2.2.10.jar;${dir}\.minecraft\libraries\com\mojang\text2speech\1.17.9\text2speech-1.17.9.jar;${dir}\.minecraft\libraries\commons-codec\commons-codec\1.15\commons-codec-1.15.jar;${dir}\.minecraft\libraries\commons-io\commons-io\2.11.0\commons-io-2.11.0.jar;${dir}\.minecraft\libraries\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;${dir}\.minecraft\libraries\io\netty\netty-buffer\4.1.82.Final\netty-buffer-4.1.82.Final.jar;${dir}\.minecraft\libraries\io\netty\netty-codec\4.1.82.Final\netty-codec-4.1.82.Final.jar;${dir}\.minecraft\libraries\io\netty\netty-common\4.1.82.Final\netty-common-4.1.82.Final.jar;${dir}\.minecraft\libraries\io\netty\netty-handler\4.1.82.Final\netty-handler-4.1.82.Final.jar;${dir}\.minecraft\libraries\io\netty\netty-resolver\4.1.82.Final\netty-resolver-4.1.82.Final.jar;${dir}\.minecraft\libraries\io\netty\netty-transport-classes-epoll\4.1.82.Final\netty-transport-classes-epoll-4.1.82.Final.jar;${dir}\.minecraft\libraries\io\netty\netty-transport-native-unix-common\4.1.82.Final\netty-transport-native-unix-common-4.1.82.Final.jar;${dir}\.minecraft\libraries\io\netty\netty-transport\4.1.82.Final\netty-transport-4.1.82.Final.jar;${dir}\.minecraft\libraries\it\unimi\dsi\fastutil\8.5.9\fastutil-8.5.9.jar;${dir}\.minecraft\libraries\net\java\dev\jna\jna-platform\5.12.1\jna-platform-5.12.1.jar;${dir}\.minecraft\libraries\net\java\dev\jna\jna\5.12.1\jna-5.12.1.jar;${dir}\.minecraft\libraries\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;${dir}\.minecraft\libraries\org\apache\commons\commons-compress\1.21\commons-compress-1.21.jar;${dir}\.minecraft\libraries\org\apache\commons\commons-lang3\3.12.0\commons-lang3-3.12.0.jar;${dir}\.minecraft\libraries\org\apache\httpcomponents\httpclient\4.5.13\httpclient-4.5.13.jar;${dir}\.minecraft\libraries\org\apache\httpcomponents\httpcore\4.4.15\httpcore-4.4.15.jar;${dir}\.minecraft\libraries\org\apache\logging\log4j\log4j-api\2.19.0\log4j-api-2.19.0.jar;${dir}\.minecraft\libraries\org\apache\logging\log4j\log4j-core\2.19.0\log4j-core-2.19.0.jar;${dir}\.minecraft\libraries\org\apache\logging\log4j\log4j-slf4j2-impl\2.19.0\log4j-slf4j2-impl-2.19.0.jar;${dir}\.minecraft\libraries\org\joml\joml\1.10.5\joml-1.10.5.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows-arm64.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows-x86.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows-arm64.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows-x86.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows-arm64.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows-x86.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows-arm64.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows-x86.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows-arm64.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows-x86.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows-arm64.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows-x86.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows-arm64.jar;${dir}\.minecraft\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows-x86.jar;${dir}\.minecraft\libraries\org\slf4j\slf4j-api\2.0.1\slf4j-api-2.0.1.jar;${dir}\.minecraft\libraries\org\ow2\asm\asm\9.6\asm-9.6.jar;${dir}\.minecraft\libraries\org\ow2\asm\asm-analysis\9.6\asm-analysis-9.6.jar;${dir}\.minecraft\libraries\org\ow2\asm\asm-commons\9.6\asm-commons-9.6.jar;${dir}\.minecraft\libraries\org\ow2\asm\asm-tree\9.6\asm-tree-9.6.jar;${dir}\.minecraft\libraries\org\ow2\asm\asm-util\9.6\asm-util-9.6.jar;${dir}\.minecraft\libraries\net\fabricmc\sponge-mixin\0.13.3+mixin.0.8.5\sponge-mixin-0.13.3+mixin.0.8.5.jar;${dir}\.minecraft\libraries\net\fabricmc\intermediary\1.20.1\intermediary-1.20.1.jar;${dir}\.minecraft\libraries\net\fabricmc\fabric-loader\0.15.11\fabric-loader-0.15.11.jar;${dir}\.minecraft\versions\0\0.jar" 
        net.fabricmc.loader.impl.launch.knot.KnotClient 
 -DFabricMcEmu=net.minecraft.client.main.Main
  -Xmn888m 
  -Xmx8000m
   --add-exports 
   cpw.mods.bootstraplauncher/cpw.mods.bootstraplauncher=ALL-UNNAMED
 --username 
 RDI 
 --version
  MC1.20.1 
  --gameDir
   "${dir}\.minecraft" 
   --assetsDir
    "${dir}\.minecraft\assets"
     --assetIndex 5
       --userType 
       msa 
       --versionType 
       RDI 
    """.trimIndent().replace("\n"," ").replace(Regex("\\s+")," ")
    val command = "$java $params2  -verbose:class"
    val pb = ProcessBuilder(*command.split(" ").toTypedArray())
    pb.redirectErrorStream(true)
        val cmd = pb.command()
    println(cmd)
    val process = pb.start()
    println("正在启动.....")
    process.inputStream.bufferedReader().forEachLine {
        println(it)
    }
    process.waitFor()

}