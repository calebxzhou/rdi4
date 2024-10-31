package calebxzhou.rdi.tutorial

data class Chapter(val name:String,val must:Boolean,val tutorials:  List<Tutorial>){
    companion object{
        val ALL = listOf(
            Chapter("基础", true,listOf(BASIC)),
            Chapter("初级生存",true,listOf(T1_STONE,T1_FIRE, T1_CERA, T1_BUILD)))
    }
}
