package calebxzhou.rdi.tutorial

object TutorialState {
    private val states = hashMapOf<String,Any>()
    operator fun set(key:String,value: Any){
        states += key to value
    }
    fun <T> get(key: String): T=states[key] as T

    fun reset() {
        states.clear()
    }
}