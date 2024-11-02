package calebxzhou.rdi.launcher

import calebxzhou.rdi.common.PINE_GREEN
import calebxzhou.rdi.util.getFileInJarUrl
import java.awt.*
import java.io.File
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JWindow
import javax.swing.SwingConstants

object SplashScreen {
    val duration: Int = 10000
    val width = 800
    val height = 480
    val textLabel = JLabel("testtesttest", SwingConstants.CENTER)
        val splash = JWindow()
    init {
        val originalImage = ImageIcon(getFileInJarUrl("assets/rdi/textures/splash_screen.jpg")).image
        val scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        //splash.isAlwaysOnTop = true
        splash.layout = BorderLayout()
        splash.contentPane.background = Color(PINE_GREEN)
        splash.contentPane.add(JLabel(ImageIcon(scaledImage)), BorderLayout.CENTER)


        textLabel.foreground = Color.WHITE
        textLabel.font = Font("Microsoft YaHei",Font.PLAIN,16)
        splash.contentPane.add(textLabel, BorderLayout.SOUTH)

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val splashSize = Dimension(width, height)
        splash.bounds = Rectangle((screenSize.width - splashSize.width) / 2,(screenSize.height - splashSize.height) / 2 , splashSize.width , splashSize.height+30)

        splash.isVisible = true
        val timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                val lastLine = File("logs/latest.log").useLines { it.lastOrNull() ?: "No data" }
                textLabel.text = lastLine
            }
        }, 0,200)
        /*thread {
            Thread.sleep(duration.toLong())
            splash.isVisible = false
            splash.dispose()
            // Continue with the rest of your program, such as showing the main window
        }*/
    }
    fun hide(){
        splash.isVisible =false
    }
}