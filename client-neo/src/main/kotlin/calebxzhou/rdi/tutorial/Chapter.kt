package calebxzhou.rdi.tutorial

data class Chapter(val name:String,val must:Boolean,val tutorials:  List<Tutorial>){
    companion object{
        val ALL = listOf(
            Chapter("基础", true,listOf(BASIC)),
            Chapter("初级生存1",true,listOf(T1_STONE,T1_FIRE, T1_CERA, T1_BUILD,T1_ARGI,/* T1_ANIMAL, T1_PET, T1_FOOD*/)),
            Chapter("初级生存2",false,listOf()),
        )

    }
}
