package calebxzhou.rdi

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val log: Logger = LogManager.getLogger("RDI")
object RDI : ModInitializer {

	override fun onInitialize() {
		log.info(Const.VERSION_STR)
	}
}